package interfaces;

import bundestag.Tagesordnungspunkt_File_Impl;
import java.util.ArrayList;

/**
 * @author Erik
 */
public interface Protokoll {
    public ArrayList<Tagesordnungspunkt_File_Impl> getTagesordnungspunkte();
    public String get_id();
}
