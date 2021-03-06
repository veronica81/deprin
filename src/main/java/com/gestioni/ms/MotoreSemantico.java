package com.gestioni.ms;

import java.util.List;

public interface MotoreSemantico {
	
	public List<String> getTipiTerreno();
	public List<String> getColture();
	public double calcolaSoglia(String terreno, String coltura);
	public double calcolaVolumeH2O(String tipoTerreno, String coltura, double u);

}
