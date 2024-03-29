/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package common.cropPicture;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import com.chongwu.R;

import common.cropPicture.ui.CropImageView;
import common.cropPicture.ui.HighlightView;

/**
 * The activity can crop specific region of interest from an image.
 */
public class CropImage extends MonitoredActivity {
    private static final String TAG = "CropImage";

    private ImageView mSaveCropPicView;

	public static final int CROP_MSG = 10;	
    public static final int CROP_MSG_INTERNAL = 100;    
      
   
    // These are various options can be specified in the intent.
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
                                                                              // used
                                                                              // with
                                                                              // mSaveUri
    private Uri mSaveUri = null;
    private int mAspectX, mAspectY; // CR: two definitions per line == sad
                                    // panda.
    private boolean mDoFaceDetection = true;
    private boolean mCircleCrop = false;
    private final Handler mHandler = new Handler();

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mOutputX, mOutputY;
    private boolean mScale;
    private boolean mScaleUp = true;

    public boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    public boolean mSaving; // Whether the "save" button is already clicked.

    private CropImageView mImageView;
    private ContentResolver mContentResolver;

    private Bitmap mBitmap;
    private MediaItem mItem;
    private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
    public HighlightView mCrop;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);        
        mContentResolver = getContentResolver();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cropimage);
        mImageView = (CropImageView) findViewById(R.id.image);

        try{
        	if(Integer.parseInt(android.os.Build.VERSION.SDK) > 11){
        		mImageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        		mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        
        Log.i(TAG, "CropImage intent=" + intent.toString());
        Log.i(TAG, "CropImage extras="+extras);

        if (extras != null) {
            if (extras.getString("circleCrop") != null) {
                mCircleCrop = true;
                mAspectX = 1;
                mAspectY = 1;
            }
            mSaveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);
            if (mSaveUri != null) {
                String outputFormatString = extras.getString("outputFormat");
                if (outputFormatString != null) {
                    mOutputFormat = Bitmap.CompressFormat.valueOf(outputFormatString);
                }
            }
            mBitmap = (Bitmap) extras.getParcelable("data");
            mAspectX = extras.getInt("aspectX");
            mAspectY = extras.getInt("aspectY");
            mOutputX = extras.getInt("outputX");
            mOutputY = extras.getInt("outputY");
            mScale = extras.getBoolean("scale", true);
            mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);
            mDoFaceDetection = extras.containsKey("noFaceDetection") ? !extras.getBoolean("noFaceDetection") : true;
        }

        if (mBitmap == null) {
            // Create a MediaItem representing the URI.
            Uri target = intent.getData();
            Log.i(TAG, "target="+target);
            String targetScheme = target.getScheme();
            int rotation = 0;

            if (targetScheme.equals("content")) {
                mItem = createMediaItemFromUri(this, target, MediaItem.MEDIA_TYPE_IMAGE);
            }
            try {
                if (mItem != null) {
                    rotation = (int) mItem.mRotation;
                    mBitmap = UriTexture.createFromUri(this, mItem.mContentUri, 1024, 1024, 0, null);
                } else {
                    mBitmap = UriTexture.createFromUri(this, target.toString(), 1024, 1024, 0, null);
                    if (targetScheme.equals("file")) {
                        ExifInterface exif = new ExifInterface(target.getPath());
                        rotation = (int) Shared.exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL));
                    }
                }
            } catch (IOException e) {
            } catch (URISyntaxException e) {
            }

            if (mBitmap != null && rotation != 0f) {
                mBitmap = Util.rotate(mBitmap, rotation);
            }
        }

        if (mBitmap == null) {
        	Log.e(TAG, "Cannot load bitmap, exiting.");
            finish();
            return;
        }

        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSaveCropPicView = (ImageView) findViewById(R.id.title_right_img_btn);
        mSaveCropPicView.setVisibility(View.VISIBLE);
       

        mSaveCropPicView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        startFaceDetection();
    }

    private void startFaceDetection() {
        if (isFinishing()) {
            return;
        }

        mImageView.setImageBitmapResetBase(mBitmap, true);

        Util.startBackgroundJob(this, null, getResources().getString(R.string.running_face_detection), new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        if (b != mBitmap && b != null) {
                            mImageView.setImageBitmapResetBase(b, true);
                            mBitmap.recycle();
                            mBitmap = b;
                        }
                        if (mImageView.getScale() == 1.0f) {
                            mImageView.center(true, true);
                        }
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mRunFaceDetection.run();
            }
        }, mHandler);
    }

    private void onSaveClicked() {
        // CR: TODO!
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return;

        if (mCrop == null) {
            return;
        }

        mSaving = true;

        Rect r = mCrop.getCropRect();

        int width = r.width(); // CR: final == happy panda!
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        Bitmap croppedImage = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(mBitmap, r, dstRect, null);
        }

        if (mCircleCrop) {
            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle. So we fill in the
            // area around the circle with alpha. Note the all important
            // PortDuff.Mode.CLEARes.
            Canvas c = new Canvas(croppedImage);
            Path p = new Path();
            p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
            c.clipPath(p, Region.Op.DIFFERENCE);
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        }

        // If the output is required to a specific size then scale or fill.
        if (mOutputX != 0 && mOutputY != 0) {
            if (mScale) {
                // Scale the image to the required dimensions.
                Bitmap old = croppedImage;
                croppedImage = Util.transform(new Matrix(), croppedImage, mOutputX, mOutputY, mScaleUp);
                if (old != croppedImage) {
                    old.recycle();
                }
            } else {

                /*
                 * Don't scale the image crop it to the size requested. Create
                 * an new image with the cropped image in the center and the
                 * extra space filled.
                 */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(b);

                Rect srcRect = mCrop.getCropRect();
                Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                int dx = (srcRect.width() - dstRect.width()) / 2;
                int dy = (srcRect.height() - dstRect.height()) / 2;

                // If the srcRect is too big, use the center part of it.
                srcRect.inset(Math.max(0, dx), Math.max(0, dy));

                // If the dstRect is too big, use the center part of it.
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

                // Draw the cropped bitmap in the center.
                canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

                // Set the cropped bitmap as the new bitmap.
                croppedImage.recycle();
                croppedImage = b;
            }
        }

        // Return the cropped image directly or save it to the specified URI.
        Bundle myExtras = getIntent().getExtras();
        if (myExtras != null && (myExtras.getParcelable("data") != null || myExtras.getBoolean("return-data"))) {
            Bundle extras = new Bundle();
            extras.putParcelable("data", croppedImage);
            setResult(RESULT_OK, (new Intent()).setAction("inline-data").putExtras(extras));
            finish();
        } else {
            final Bitmap b = croppedImage;
            final Runnable save = new Runnable() {
                public void run() {
                    saveOutput(b);
                }
            };
            Util.startBackgroundJob(this, null, getResources().getString(R.string.saving_image), save, mHandler);
        }
    }

    @SuppressLint("UseValueOf")
	private void saveOutput(Bitmap croppedImage) {
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 75, outputStream);
                }
                // TODO ExifInterface write
            } catch (IOException ex) {
            	Log.e(TAG, "Cannot open file: " + mSaveUri);
            } finally {
                Util.closeSilently(outputStream);
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString()).putExtras(extras));
        } else {
            Bundle extras = new Bundle();
            extras.putString("rect", mCrop.getCropRect().toString());
            if (mItem == null) {
                // CR: Comments should be full sentences.
                // this image doesn't belong to the local data source
                // we can add it locally if necessary
            } else {
                File oldPath = new File(mItem.mFilePath);
                File directory = new File(oldPath.getParent());

                int x = 0;
                String fileName = oldPath.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));

                // Try file-1.jpg, file-2.jpg, ... until we find a filename
                // which
                // does not exist yet.
                while (true) {
                    x += 1;
                    String candidate = directory.toString() + "/" + fileName + "-" + x + ".jpg";
                    boolean exists = (new File(candidate)).exists();
                    if (!exists) { // CR: inline the expression for exists
                                   // here--it's clear enough.
                        break;
                    }
                }

                MediaItem item = mItem;
                String title = fileName + "-" + x;
                String finalFileName = title + ".jpg";
                int[] degree = new int[1];
                Double latitude = null;
                Double longitude = null;
                if (item.isLatLongValid()) {
                    latitude = new Double(item.mLatitude);
                    longitude = new Double(item.mLongitude);
                }
                Uri newUri = ImageManager.addImage(mContentResolver, title,
                        item.mDateAddedInSec, item.mDateTakenInMs, latitude,
                        longitude, directory.toString(), finalFileName,
                        croppedImage, null, degree);
                if (newUri != null) {
                    setResult(RESULT_OK, new Intent().setAction(newUri.toString()).putExtras(extras));
                } else {
                    setResult(RESULT_OK, new Intent().setAction(null));
                }
            }
        }
        croppedImage.recycle();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }    
    
    @Override
    protected void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
    }

    @Override
    protected void onDestroy() {
        //mApp.shutdown();
        super.onDestroy();
    }

    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // CR: sentences!
            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);
            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth(); // CR: F => f (or change
                                                      // all f to F).
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                       // mCrop.setMode(ModifyMode.Grow);
                    }

                    if (mNumFaces > 1) {
                        // CR: no need for the variable t. just do
                        // Toast.makeText(...).show().
                        Toast t = Toast.makeText(CropImage.this, R.string.multiface_crop_help, Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            });
        }
    };
    
    private static MediaItem createMediaItemFromUri(Context context, Uri target, int mediaType) {
        MediaItem item = null;
        long id = ContentUris.parseId(target);
        ContentResolver cr = context.getContentResolver();
        String whereClause = Images.ImageColumns._ID + "=" + Long.toString(id);
        try {
            final Uri uri = (mediaType == MediaItem.MEDIA_TYPE_IMAGE)
                    ? Images.Media.EXTERNAL_CONTENT_URI
                    : Video.Media.EXTERNAL_CONTENT_URI;
            final String[] projection = (mediaType == MediaItem.MEDIA_TYPE_IMAGE)
                    ? CacheService.PROJECTION_IMAGES
                    : CacheService.PROJECTION_VIDEOS;
            Cursor cursor = cr.query(uri, projection, whereClause, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    item = new MediaItem();
                    CacheService.populateMediaItemFromCursor(item, cr, cursor, uri.toString() + "/");
                }
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
        	Log.i(TAG, "createMediaItemFromUri exception="+e.toString());
        }
        item.mId = id;
        return item;
    }
}
