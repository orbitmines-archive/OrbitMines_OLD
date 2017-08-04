package com.orbitmines.api.spigot;

import com.orbitmines.api.Data;
import com.orbitmines.api.Server;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public interface OrbitMinesServer {

    /* Server Type */
    Server getServerType();

    /* What data should be parsed from the players stats */
    Data[] getDataToParse();


}
