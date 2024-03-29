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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.*;

/**
 * ImageManager is used to retrieve and store images in the media content
 * provider.
 */
public class ImageManager {
    private static final String TAG = "ImageManager";

    private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;

    /**
     * Enumerate type for the location of the images in gallery.
     */
    public static enum DataLocation {
        NONE, INTERNAL, EXTERNAL, ALL
    }

    public static final String IMAGE_JPEG_TYPE = "image/jpeg";

    public static final Bitmap DEFAULT_THUMBNAIL = Bitmap.createBitmap(32, 32, Bitmap.Config.RGB_565);
    public static final Bitmap NO_IMAGE_BITMAP = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);

    public static final int SORT_ASCENDING = 1;
    public static final int SORT_DESCENDING = 2;

    public static final int INCLUDE_IMAGES = (1 << 0);
    public static final int INCLUDE_DRM_IMAGES = (1 << 1);
    public static final int INCLUDE_VIDEOS = (1 << 2);

    public static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }
    
    public static int roundOrientation(int orientationInput) {
        int orientation = orientationInput;
        if (orientation == -1) {
            orientation = 0;
        }

        orientation = orientation % 360;
        int retVal;
        if (orientation < (0 * 90) + 45) {
            retVal = 0;
        } else if (orientation < (1 * 90) + 45) {
            retVal = 90;
        } else if (orientation < (2 * 90) + 45) {
            retVal = 180;
        } else if (orientation < (3 * 90) + 45) {
            retVal = 270;
        } else {
            retVal = 0;
        }

        return retVal;
    }

    /**
     * @return true if the mimetype is an image mimetype.
     */
    public static boolean isImageMimeType(String mimeType) {
        return mimeType.startsWith("image/");
    }

    /**
     * @return true if the mimetype is a video mimetype.
     */
    public static boolean isVideoMimeType(String mimeType) {
        return mimeType.startsWith("video/");
    }

    public static void setImageSize(ContentResolver cr, Uri uri, long size) {
        ContentValues values = new ContentValues();
        values.put(Images.Media.SIZE, size);
        cr.update(uri, values, null, null);
    }

    /**
     * Used save image info into media database.
     *
     * @param cr The application ContentResolover
     *            {@link #Activity.getContentResolver()}
     * @param title This title will show in Gallery-Picture-Details.
     * @param dateTaken Photo take time.
     * @param type Image Type ("image/jpeg")
     * @param orientation Orientation values(0, 90, 180, 270)
     * @param directory
     * @param filename That filename is what will be handed to Gmail when a user
     *            shares a photo. Gmail gets the name of the picture attachment
     *            from the "DISPLAY_NAME" field
     * @param location
     * @return if file no exist, return null.
     */
    public static Uri addImage(ContentResolver cr, String title, long dateTaken, String type, int orientation,
                               File file, Location location) {
        String filePath = file.getPath();
        String filename = file.getName();
        // Read file size.
        long size = file.length();

        ContentValues values = new ContentValues(9);
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, filename);
        values.put(Images.Media.DATE_TAKEN, dateTaken);
        values.put(Images.Media.MIME_TYPE, type);
        values.put(Images.Media.ORIENTATION, orientation);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        if (location != null) {
            values.put(Images.Media.LATITUDE, location.getLatitude());
            values.put(Images.Media.LONGITUDE, location.getLongitude());
        }

        if (false) {
            Log.i(TAG, "Save image info :");
            Log.i(TAG, "Title(" + title + ")");
            Log.i(TAG, "Display name(" + filename + ")");
            Log.i(TAG, "Date taken(" + dateTaken + ")");
            Log.i(TAG, "Mime Type(" + type + ")");
            Log.i(TAG, "Orientation(" + orientation + ")");
            Log.i(TAG, "Data(" + filePath + ")");
            Log.i(TAG, "Size(" + size + ")");
            if (location != null) {
                Log.i(TAG, "Latitude(" + location.getLatitude() + ")");
                Log.i(TAG, "Longitude(" + location.getLongitude() + ")");
            }
        }
        Uri uri = null;
        try {
            /*
            insert在部分手机上出现
            java.lang.IllegalArgumentException: Unknown URL content://media/external/images/media
            异常的原因是系统ContentProvider无法获取到database
            https://code.google.com/p/android/issues/detail?id=21792
            https://code.google.com/p/android/issues/detail?id=2861
            现无方法作Walk around
            */
            uri = cr.insert(STORAGE_URI, values);
            return uri;
        } catch (Exception e) {
            // gionee77_cu_ics2/gionee77_cu_ics2:4.0.4/IMM76D
            // 在调用insert方法时，会出现native不能创建文件的exception，这里仅捕捉异样
            // 并返回null

            // insert失败后，照片其他逻辑不受影响，仅在系统相册中无法看到该照片
            Log.w(TAG, e);
            Log.w(TAG, "Insert to sysdb failed, just skip it.");
            return null;
        }
    }

    /**
     * Stores a bitmap or a jpeg byte array to a file (using the specified
     * directory and filename). Also add an entry to the media store for
     * this picture. The title, dateTaken, location are attributes for the
     * picture. The degree is a one element array which returns the orientation
     * of the picture.
    */
    public static Uri addImage(ContentResolver cr, String title, long dateAdded,
            long dateTaken, Double latitude, Double longitude, String directory,
            String filename, Bitmap source, byte[] jpegData, int[] degree) {
        // We should store image data earlier than insert it to ContentProvider,
        // otherwise we may not be able to generate thumbnail in time.
        OutputStream outputStream = null;
        String filePath = directory + "/" + filename;
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.w(TAG, "Create dir failed!");
                }
            }
            File file = new File(directory, filename);
            outputStream = new FileOutputStream(file);
            if (source != null) {
                source.compress(CompressFormat.JPEG, 75, outputStream);
                degree[0] = 0;
            } else {
                outputStream.write(jpegData);
                degree[0] = getExifOrientation(filePath);
            }
        } catch (FileNotFoundException ex) {
            Log.w(TAG, ex);
            return null;
        } catch (IOException ex) {
            Log.w(TAG, ex);
            return null;
        } finally {
            Util.closeSilently(outputStream);
        }

        // Read back the compressed file size.
        long size = new File(directory, filename).length();

        ContentValues values = new ContentValues(11);
        values.put(Images.Media.TITLE, title);

        // That filename is what will be handed to Gmail when a user shares a
        // photo. Gmail gets the name of the picture attachment from the
        // "DISPLAY_NAME" field.
        values.put(Images.Media.DISPLAY_NAME, filename);
        values.put(Images.Media.DATE_TAKEN, dateTaken);
        values.put(Images.Media.DATE_MODIFIED, dateTaken);
        values.put(Images.Media.DATE_ADDED, dateAdded);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.ORIENTATION, degree[0]);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        if (latitude != null && longitude != null) {
            values.put(Images.Media.LATITUDE, latitude.floatValue());
            values.put(Images.Media.LONGITUDE, longitude.floatValue());
        }

        return cr.insert(STORAGE_URI, values);
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        Log.w(TAG, "Invalid orientation:" + orientation);
                        break;
                }

            }
        }
        return degree;
    }
    static boolean isSingleImageMode(String uriString) {
        return !uriString.startsWith(Images.Media.EXTERNAL_CONTENT_URI.toString())
                && !uriString.startsWith(Images.Media.INTERNAL_CONTENT_URI.toString());
    }

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }

    public static boolean quickHasStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean hasStorage() {
        return hasStorage(true);
    }

    public static boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static Cursor query(final ContentResolver resolver, final Uri uri, final String[] projection,
            final String selection, final String[] selectionArgs, final String sortOrder) {
        try {
            if (resolver == null) {
                return null;
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }

    }

    public static boolean isMediaScannerScanning(final ContentResolver cr) {
        boolean result = false;
        final Cursor cursor = query(cr, MediaStore.getMediaScannerUri(), new String[] { MediaStore.MEDIA_SCANNER_VOLUME }, null,
                null, null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                result = "external".equals(cursor.getString(0));
            }
            cursor.close();
        }
        return result;
    }

    public static String getLastImageThumbPath() {
        return Environment.getExternalStorageDirectory().toString() + "/DCIM/.thumbnails/image_last_thumb";
    }

    public static String getLastVideoThumbPath() {
        return Environment.getExternalStorageDirectory().toString() + "/DCIM/.thumbnails/video_last_thumb";
    }
}
