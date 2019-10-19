/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/

package alma.obops.template;

import alma.lifecycle.stateengine.constants.StateFlag;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static String[] MANUAL_SUBSTATES = new String[] {
            StateFlag.MANUALCALIBRATION.toString(),
            StateFlag.MANUALSINGLEDISH.toString(),
            StateFlag.MANUALIMAGING.toString(),
            StateFlag.MANUALCOMBINATION.toString(),
            StateFlag.MANUALCALANDIMG.toString()};
    public static String[] PIPELINE_SUBSTATES = new String[] {
            StateFlag.PIPELINECALIBRATION.toString(),
            StateFlag.PIPELINESINGLEDISH.toString(),
            StateFlag.PIPELINEIMAGING.toString(),
            StateFlag.PIPELINECALANDIMG.toString()};

    public enum DataAvailability {
        na ("DATA_AT_NA"), eu ("DATA_AT_EU"), ea ("DATA_AT_EA"), jao("DATA_AT_JAO");

        private final String name;
        DataAvailability(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }

        public static DataAvailability getByName(String name){
            for (DataAvailability da: DataAvailability.values()){
                if (da.name.equals(name)) {return da;}
            }
            return null;
        }
    };

    public enum DARED {
        PipelineCalibration ("calPipeIF"),
        PipelineCalAndImg ("redPipeIF"),
        PipelineSingleDish ("redPipeSD"),
        PipelineImaging("imgPipeIF");

        private final String name;
        DARED(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }

        public static DARED getByName(String name){
            for (DARED da: DARED.values()){
                if (da.name.equals(name)) {return da;}
            }
            return null;
        }
    };

    public enum QueueDict {
        PipelineCalibration ("CalPipe"),
        PipelineCalAndImg ("RedPipe"),
        PipelineSingleDish ("RedPipe"),
        PipelineImaging("RedPipe");

        private final String name;
        QueueDict(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }

        public static QueueDict getByName(String name){
            for (QueueDict da: QueueDict.values()){
                if (da.name.equals(name)) {return da;}
            }
            return null;
        }
    };


    public enum MemoryTypes {
        _12M_cal,
        _7M,
        _SD,
        _12M;
    };

    public static Map<String, List<String>> DATA_REQUIRED_FOR_RECIPE = new HashMap<>();
    static {
        DATA_REQUIRED_FOR_RECIPE.put(StateFlag.PIPELINECALIBRATION.toString(),
                Arrays.asList("ASDM","ASDM_CAL","ASDM_CAL_IMG"));
        DATA_REQUIRED_FOR_RECIPE.put(StateFlag.PIPELINECALANDIMG.toString(),
                Arrays.asList("ASDM","ASDM_CAL","ASDM_CAL_IMG"));
        DATA_REQUIRED_FOR_RECIPE.put(StateFlag.PIPELINESINGLEDISH.toString(),
                Arrays.asList("ASDM","ASDM_CAL","ASDM_CAL_IMG"));
        DATA_REQUIRED_FOR_RECIPE.put(StateFlag.PIPELINEIMAGING.toString(),
                Arrays.asList("ASDM_CAL","ASDM_CAL_IMG"));
    }



}
