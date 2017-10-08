package com.digiota.fotodirs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdiamand on 1/30/17.
 */

public class FotoDirInfo {
    public String dirName ;
    public int    fotoCount ;
    public List<FotoFileInfo> fotoFileInfoList ;
    FotoDirInfo(String name,int count) {
        dirName = name ;
        fotoCount = count ;
        fotoFileInfoList = new ArrayList<FotoFileInfo>() ;
    }
    public void incrementCount () {
        fotoCount++ ;
    }
}
