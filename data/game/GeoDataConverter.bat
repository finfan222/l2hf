@echo off
title L2D geodata converter

java -Xmx512m -cp ./../libs/* org.lineage.tools.geodataconverter.GeoDataConverter

pause
