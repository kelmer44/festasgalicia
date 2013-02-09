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
		
		public static final String url_subtipoevento = ServerName + "SubtipoEvento";
		public static final String url_evento = ServerName + "Evento";
		public static final String url_municipio = ServerName + "Municipio";
		
				
		//Common tags
		public static final String TAG_SUCCESS = "success";
		public static final String TAG_MESSAGE = "message";
		public static final String TAG_DATA = "data";
		public static final String TAG_DATA_COUNT = "totalCount";
		
		
		//Array de subtipos
		public static final String TAG_ARRAY_SUBTITPO = "subtipoEvento";
		
		//Subtipo
		public static final String TAG_SUBTIPO_ID_SUBTIPO = "ID_SUBTIPO_EVENTO";
		public static final String TAG_SUBTIPO_DESC_SUBTIPO_EVENTO = "DESCRIPCION";
		
		
		//Array de eventos
		public static final String TAG_ARRAY_EVENTO = "evento";
		
		//Evento
		public static final String TAG_EVENTO_ID_EVENTO = "ID_EVENTO";
		public static final String TAG_EVENTO_ID_EVENTO_RELACIONADO = "ID_EVENTO_RELACIONADO";
		public static final String TAG_EVENTO_ID_CATEGORIA_EVENTO = "ID_CATEGORIA_EVENTO";
		public static final String TAG_EVENTO_ID_TIPO_EVENTO = "ID_TIPO_EVENTO";
		public static final String TAG_EVENTO_ID_SUBTIPO_EVENTO = "ID_SUBTIPO_EVENTO";
		public static final String TAG_EVENTO_NOMBRE = "NOMBRE";
		public static final String TAG_EVENTO_DESCRIPCION = "DESCRIPCION";
		public static final String TAG_EVENTO_ID_POBLACION = "ID_POBLACION";
		public static final String TAG_EVENTO_DESCRIPCION_LUGAR = "DESCRIPCION_LUGAR";
		public static final String TAG_EVENTO_LATITUD = "LATITUD";
		public static final String TAG_EVENTO_LONGITUD = "LONGITUD";
		public static final String TAG_EVENTO_FECHA_HORA_INICIO = "FECHA_HORA_INICIO";
		public static final String TAG_EVENTO_FECHA_HORA_FIN = "FECHA_HORA_FIN";
		public static final String TAG_EVENTO_IMAGEN_PRINCIPAL = "IMAGEN_PRINCIPAL";
		public static final String TAG_EVENTO_IMAGEN_LISTA = "IMAGEN_LISTA";
		public static final String TAG_EVENTO_OTROS = "OTROS";
		public static final String TAG_EVENTO_ID_MUNICIPIO = "ID_MUNICIPIO";
		public static final String TAG_EVENTO_MUNICIPIO = "iDMUNICIPIO";
		public static final String TAG_EVENTO_CATEGORIA_EVENTO = "iDCATEGORIAEVENTO";
		public static final String TAG_EVENTO_SUBTIPO_EVENTO = "iDSUBTIPOEVENTO";
		public static final String TAG_EVENTO_TIPO_EVENTO = "iDTIPOEVENTO";
		
		
		//Array de Municipios
		public static final String TAG_ARRAY_MUNICIPIO = "municipio";
		
		//Municipio
		public static final String TAG_MUNICIPIO_ID_MUNICIPIO = "ID_MUNICIPIO";
		public static final String TAG_MUNICIPIO_ID_PROVINCIA = "ID_PROVINCIA";
		public static final String TAG_MUNICIPIO_NOMBRE = "NOMBRE";
		public static final String TAG_MUNICIPIO_LATITUD = "latitud";
		public static final String TAG_MUNICIPIO_LONGITUD = "longitud";
		public static final String TAG_MUNICIPIO_PROVINCIA = "iDPROVINCIA";
		
		
	
}
