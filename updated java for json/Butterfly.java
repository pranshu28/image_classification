package com.github.nf1213.camera;

/**
 * Created by avinaash on 2/4/17.
 */

public class Butterfly {



    private String kingdom;
    private String common_name;
    private String order;
    private String class_name;
    private String phylum;
    private String sci_name;
    private String species_name;
    private String wiki_str;
    private String family;
    private String genus;
    private String loc;



    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getSci_name() {
        return sci_name;
    }

    public void setSci_name(String sci_name) {
        this.sci_name = sci_name;
    }

    public String getSpecies_name() {
        return species_name;
    }

    public void setSpecies_name(String species_name) {
        this.species_name = species_name;
    }

    public String getWiki_str() {
        return wiki_str;
    }

    public void setWiki_str(String wiki_str) {
        this.wiki_str = wiki_str;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }


    @Override
    public String toString(){
        return kingdom;
    }
    public void Butterfly(String kingdom, String common_name, String order, String class_name, String phylum, String sci_name, String species_name, String wiki_str, String family, String genus, String loc) {

        this.kingdom = kingdom;
        this.common_name = common_name;
        this.order = order;
        this.class_name = class_name;
        this.phylum = phylum;
        this.sci_name = sci_name;
        this.species_name = species_name;
        this.wiki_str = wiki_str;
        this.family = family;
        this.genus = genus;
        this.loc = loc;
    }


}
