package com.chongwu.utils.common.media;

import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class MediaUtil {
	private static HashMap<Integer, MediaPlayer> soundMap = new HashMap<Integer, MediaPlayer>();
	
	public static void playSound(final Context context, final int resId) {
		playSound(context, resId, null, null);
	}

	public static void playSound(final Context context, final int resId, final OnPreparedListener startlistener,
			final OnCompletionListener endListener) {
		new Thread() {
			@Override
			public void run() {
				try {
					MediaPlayer mediaPlayer = soundMap.get(resId);
					if (mediaPlayer == null) {
						mediaPlayer = MediaPlayer.create(context, resId);
						soundMap.put(resId, mediaPlayer);
					}
					// this.join();
					if (startlistener != null) {

						mediaPlayer.setOnPreparedListener(startlistener);
					}

					if (endListener != null) {
						mediaPlayer.setOnCompletionListener(endListener);
					}

					mediaPlayer.start();

				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
