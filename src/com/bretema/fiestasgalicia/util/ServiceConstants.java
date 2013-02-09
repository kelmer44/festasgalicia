package com.bretema.fiestasgalicia.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;

public final class ServiceConstants {
	
		//server name
		public static final String ServerName = "http://www.bretemasoftware.com/festasCRUD/index.php/api/";
		//public static final String ServerName = "http://77.27.50.46:8080/buses/";
		
		// recoge la lista de paradas asociadas a una línea, si se pasa el parametro ida recoge solo las de un viaje de la línea
		public static final String url_subtipoevento = ServerName + "SubtipoEvento";
		// recoge la lista de puntos geolocalizados asociados a una línea, si se pasa el parametro ida recoge solo las de un viaje de la línea		
		public static final String url_evento = ServerName + "Evento";
		
				
		//Common tags
		public static final String TAG_SUCCESS = "success";
		public static final String TAG_MESSAGE = "message";
		public static final String TAG_DATA = "data";
		public static final String TAG_DATA_COUNT = "totalCount";
		
		
		//Array de líneas
		public static final String TAG_ARRAY_SUBTITPO = "subtipoEvento";
		
		//Categoria
		public static final String TAG_ID_SUBTIPO_EVENTO = "ID_SUBTIPO_EVENTO";
		public static final String TAG_DESC_SUBTIPO_EVENTO = "DESCRIPCION";
		
		
		
	
}
