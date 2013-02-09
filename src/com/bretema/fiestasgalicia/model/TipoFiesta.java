package com.bretema.fiestasgalicia.model;

public enum TipoFiesta {
	Patronal(0), Gastronómica(1), Medieval(2), Tradicional(3);

	TipoFiesta(int position) {
		this.position = position;
	}

	private int	position;

	public int getPosition() {
		return position;
	}

	public static TipoFiesta getByIndex(int index) {
		TipoFiesta[] tiposFiesta = TipoFiesta.values();
		for (TipoFiesta tipo : tiposFiesta) {
			if (tipo.getPosition() == index) {
				return tipo;
			}
		}

		return null;
	}

}
