package Parse;

import java.util.ArrayList;

public class Countries implements IRules {

    String [] countries196 = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua Deps",
            "Argentina", "Armenia","Australia" ,"Austria" ,"Azerbaijan","Barbados","Belarus" ,"Cambodia","Cameroon",
            "Argentina","Bahamas","Bahrain", "Bangladesh" , "Belgium" ,"Belize" ,"Benin" , "Canada",
            "Bhutan" , "Bolivia","Bosnia Herzegovina" ,"Bosnia Herzegovina" ,"Botswana","Chad", "Chile",
            "Brazil","Brunei" ,"Bulgaria" ,"Burkina" , "Burundi","Cape Verde", "Central African Rep","China",
            "Colombia","Comoros","Congo","Congo","Democratic Rep","Costa Rica","Croatia","Cuba","Cyprus",
            "Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","East Timor","Ecuador",
            "Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Fiji","Finland",
            "France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala","Guinea",
            "Guinea-Bissau","Guyana","Haiti","Honduras","Hungary","Iceland","India","Indonesia","Iran",
            "Iraq","Ireland","Israel","Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan",
            "Kenya","Kiribati","Korea North","Korea South","Kosovo","Kuwait","Kyrgyzstan","Laos","Latvia",
            "Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macedonia",
            "Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Mauritania",
            "Mauritius","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro",
            "Morocco","Mozambique","Myanmar, {Burma}","Namibia","Nauru","Nepal","Netherlands","New Zealand",
            "Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palau","Panama","Papua New Guinea",
            "Paraguay","Peru","Philippines","Poland","Portugal","Qatar","Romania","Russian Federation",
            "Rwanda","St Kitts & Nevis","St Lucia","Saint Vincent & the Grenadines","Samoa","San Marino",
            "Sao Tome & Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore",
            "Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","Spain","Sri Lanka","Sudan",
            "Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Togo",
            "Tonga","Trinidad & Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","Ukraine",
            "United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan","Vanuatu","Vatican City",
            "Venezuela","Vietnam","Yemen","Zambia","Zimbabwe"};

    ArrayList<String> allCountries;

    public Countries(){
        allCountries = new ArrayList<>();
        for (String country: countries196) {
            allCountries.add(country);
        }
    }

    /**
     *      * Gets a word and checks if they are match the term conditions
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word) {
        if(word!=null && word.length()>0){
            return allCountries.contains(word);
        }
        return false;
    }

    /**
     * Gets two words and checks if they are match the term conditions
     * @param word1
     * @param word2
     * @return
     */
    @Override
    public boolean amIThis(String word1, String word2) {
        String word = word1+" "+word2;
        if(word!=null && word.length()>0){
            return allCountries.contains(word);
        }
        return false;
    }

    /**
     * Gets three words and checks if they are match the term conditions
     * @param word1
     * @param word2
     * @param word3
     * @return
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        String word = word1+" "+word2+" "+word3;
        if(word!=null && word.length()>0){
            return allCountries.contains(word);
        }
        return false;
    }

    /**
     * Gets four words and checks if they are match the term conditions
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return false;
    }

    /**
     * Gets a word and create the right term for her.
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        if(word != null & word.length()>0){
            if (amIThis(word)){
                Term term = new Term(word, "Countries");
                return term;
            }
        }
        return null;
    }
}
