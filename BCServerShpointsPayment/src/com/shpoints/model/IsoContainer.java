package com.shpoints.model;

import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 对XML-source进行转包不转header
 * <p>组织包报文格式类.</p>
 *
 */
@XmlRootElement(name="simple8583-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class IsoContainer extends Vector<IsoPackage>{

	private static final long serialVersionUID = -4470632464434928749L;

	@XmlElement(name="package")
    public Vector<IsoPackage> getPackages(){
        return this;
    }

    public IsoPackage getPackByMti(String mti){
        for(IsoPackage pack:this){
            if(pack.getMti().equals(mti)){
                return pack;
            }
        }
        return null;
    }

}