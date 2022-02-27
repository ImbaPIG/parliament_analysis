package interfaces;

import bundestag.Rede_File_Impl;

import java.util.ArrayList;

/**
 * @author Erik
 */
public interface Tagesordnungspunkt {
    public ArrayList<Rede_File_Impl> getReden();

    public String getText();

    public String getTopID();
}
