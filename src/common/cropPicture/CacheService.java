/*
 * Copyright (C) 2009 The Android Open Source Project
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
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

public final class CacheService{
    // Must preserve order between these indices and the order of the terms in
    // INITIAL_PROJECTION_IMAGES and
    // INITIAL_PROJECTION_VIDEOS.
    public static final int MEDIA_ID_INDEX = 0;
    public static final int MEDIA_CAPTION_INDEX = 1;
    public static final int MEDIA_MIME_TYPE_INDEX = 2;
    public static final int MEDIA_LATITUDE_INDEX = 3;
    public static final int MEDIA_LONGITUDE_INDEX = 4;
    public static final int MEDIA_DATE_TAKEN_INDEX = 5;
    public static final int MEDIA_DATE_ADDED_INDEX = 6;
    public static final int MEDIA_DATE_MODIFIED_INDEX = 7;
    public static final int MEDIA_DATA_INDEX = 8;
    public static final int MEDIA_ORIENTATION_OR_DURATION_INDEX = 9;
    public static final int MEDIA_BUCKET_ID_INDEX = 10;
    public static final String[] PROJECTION_IMAGES = new String[] { Images.ImageColumns._ID, Images.ImageColumns.TITLE,
            Images.ImageColumns.MIME_TYPE, Images.ImageColumns.LATITUDE, Images.ImageColumns.LONGITUDE,
            Images.ImageColumns.DATE_TAKEN, Images.ImageColumns.DATE_ADDED, Images.ImageColumns.DATE_MODIFIED,
            Images.ImageColumns.DATA, Images.ImageColumns.ORIENTATION, Images.ImageColumns.BUCKET_ID };

    public static final String[] PROJECTION_VIDEOS = new String[] { Video.VideoColumns._ID, Video.VideoColumns.TITLE,
            Video.VideoColumns.MIME_TYPE, Video.VideoColumns.LATITUDE, Video.VideoColumns.LONGITUDE, Video.VideoColumns.DATE_TAKEN,
            Video.VideoColumns.DATE_ADDED, Video.VideoColumns.DATE_MODIFIED, Video.VideoColumns.DATA, Video.VideoColumns.DURATION,
            Video.VideoColumns.BUCKET_ID };
    
    
    public static String getCachePath(final String subFolderName) {
        return Environment.getExternalStorageDirectory() + "/Android/data/com.cooliris.media/cache/" + subFolderName;
    }

    public static void populateMediaItemFromCursor(final MediaItem item, final ContentResolver cr, final Cursor cursor,
            final String baseUri) {
        item.mId = cursor.getLong(CacheService.MEDIA_ID_INDEX);
        item.mCaption = cursor.getString(CacheService.MEDIA_CAPTION_INDEX);
        item.mMimeType = cursor.getString(CacheService.MEDIA_MIME_TYPE_INDEX);
        item.mLatitude = cursor.getDouble(CacheService.MEDIA_LATITUDE_INDEX);
        item.mLongitude = cursor.getDouble(CacheService.MEDIA_LONGITUDE_INDEX);
        item.mDateTakenInMs = cursor.getLong(CacheService.MEDIA_DATE_TAKEN_INDEX);
        item.mDateAddedInSec = cursor.getLong(CacheService.MEDIA_DATE_ADDED_INDEX);
        item.mDateModifiedInSec = cursor.getLong(CacheService.MEDIA_DATE_MODIFIED_INDEX);
        if (item.mDateTakenInMs == item.mDateModifiedInSec) {
            item.mDateTakenInMs = item.mDateModifiedInSec * 1000;
        }
        item.mFilePath = cursor.getString(CacheService.MEDIA_DATA_INDEX);
        if (baseUri != null)
            item.mContentUri = baseUri + item.mId;
        final int itemMediaType = item.getMediaType();
        final int orientationDurationValue = cursor.getInt(CacheService.MEDIA_ORIENTATION_OR_DURATION_INDEX);
        if (itemMediaType == MediaItem.MEDIA_TYPE_IMAGE) {
            item.mRotation = orientationDurationValue;
        } else {
            item.mDurationInSec = orientationDurationValue;
        }
    } 
}
