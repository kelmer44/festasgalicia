package com.bretema.fiestasgalicia.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public class FiestasContract {
			
			
			
	interface FiestasColumns {

		String FIESTA_ID = "ID_EVENTO";
		String EVENTO_RELACIONADO = "ID_EVENTO_RELACIONADO";
		String ID_CATEGORIA = "ID_CATEGORIA_EVENTO";
		String ID_TIPO = "ID_TIPO_EVENTO";
		String ID_SUBTIPO = "ID_SUBTIPO_EVENTO";
		String NOMBRE = "NOMBRE";
		String DESCRIPCION = "DESCRIPCION";
		String ID_POBLACION = "ID_POBLACION";
		String DESCRIPCION_LUGAR = "DESCRIPCION_LUGAR";
		String LATITUD = "LATITUD";
		String LONGITUD = "LONGITUD";
		String FECHA_INICIO = "FECHA_HORA_INICIO";
		String FECHA_FIN = "FECHA_HORA_FIN";
		String IMAGEN_PRINCIPAL = "IMAGEN_PRINCIPAL";
		String IMAGEN_LISTA = "IMAGEN_LISTA";
		String OTROS = "OTROS";
		String ID_MUNICIPIO = "ID_MUNICIPIO";
		String MUNICIPIO = "iDMUNICIPIO";
		String CATEGORIA = "iDCATEGORIAEVENTO";
		String SUBTIPO = "iDSUBTIPOEVENTO";
		String TIPO = "iDTIPOEVENTO";
	}

	interface Municipio {
		String MUNICIPIO_ID = "ID_MUNICIPIO";
		String ID_PROVINCIA = "ID_PROVINCIA";
		String NOMBRE = "NOMBRE";
		String LATITUD = "latitud";
		String LONGITUD = "longitud";
		String PROVINCIA = "iDPROVINCIA";
	}
	
	interface Subtipo {
		String SUBTIPO_ID = "ID_SUBTIPO_EVENTO";
		String DESCRIPCION = "DESCRIPCION";
	}
	
	private static final String ServerName = "http://www.bretemasoftware.com/festasCRUD/index.php/api/";
	private static final Uri BASE_CONTENT_URI = Uri.parse(ServerName);
	
	public static final String PATH_FIESTAS = "Evento";
	public static final String PATH_SUBTIPOS = "SubtipoEvento";
	public static final String PATH_MUNICIPIO = "Municipio";
	
	
	public static class Fiestas implements FiestasColumns, BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FIESTAS)
                .build();

        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");

        /** Use if multiple items get returned */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.seriesguide.show";

        /** Use if a single item is returned */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.seriesguide.show";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = FiestasColumns.NOMBRE + " ASC";

        //public static final String SELECTION_FAVORITES = " AND " + Shows.FAVORITE + "=1";

        public static Uri buildFiestaUri(String showId) {
            return CONTENT_URI.buildUpon().appendPath(showId).build();
        }

        public static String getFiestaId(Uri uri) {
            return uri.getLastPathSegment();
        }
	}
	
	
	public static class Subtipos implements FiestasColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBTIPOS)
                .build();

        /** Use if multiple items get returned */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.seriesguide.season";

        /** Use if a single item is returned */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.seriesguide.season";

        public static Uri buildSeasonUri(String seasonId) {
            return CONTENT_URI.buildUpon().appendPath(seasonId).build();
        }

        public static String getSubtipoId(Uri uri) {
            return uri.getLastPathSegment();
        }
		
		
		
	}
}
