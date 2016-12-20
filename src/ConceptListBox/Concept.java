/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConceptListBox;

import java.io.Serializable;

/**
 *
 * @author thehaohcm
 */
public class Concept implements Serializable {
    private String suffixDomain;
    private String nameOfPC;

    public Concept(String suffixDomain, String nameOfPC) {
        this.suffixDomain = suffixDomain;
        this.nameOfPC = nameOfPC;
    }
    
    public void setSuffixDomain(String suffixDomain){
        this.suffixDomain = suffixDomain;
    }
    
    public String getSuffixDomain() {
        return suffixDomain;
    }

    public String getNameOfPC() {
        return nameOfPC;
    }
    
    public void ToString(){
        System.out.println("suffixDomain: "+suffixDomain+" - value: "+nameOfPC);
    }
}
