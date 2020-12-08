package com.example.baseactivity;

enum Difficult{beginner, advanced, intermediate, upper_intermediate, none}

public class Dictionary {

    public Difficult difficult;
    public String En, Ru, Group, Subgroup, Rubric, Transcription;
    public int rating, Popularity;
    public  boolean Hard, Favorite;

    public Dictionary(String _En, String _Ru, String _rating){
        En=_En;
        Ru=_Ru;
        rating = Integer.parseInt(_rating);
        Hard = false;
        Favorite = false;
    }

    public  Dictionary(Dictionary dictionary){
        difficult=dictionary.difficult;
        En=dictionary.En;
        Ru=dictionary.Ru;
        Group=dictionary.Group;
        Subgroup=dictionary.Subgroup;
        Rubric=dictionary.Rubric;
        Transcription=dictionary.Transcription;
        rating=dictionary.rating;
        Popularity=dictionary.Popularity;
        Hard=dictionary.Hard;
        Favorite=dictionary.Favorite;
    }
    public int SortByEn(Dictionary dictionary1, Dictionary dictionary2){
        return dictionary1.En.compareTo(dictionary2.En);
    }
    public Dictionary(String params[]){//9 params
        Ru=params[0];
        En=params[1];
        Transcription=params[2];
        Popularity=Integer.valueOf(params[3]);
        if(params[4].equals("beginner"))
            difficult=Difficult.beginner;
        else if(params[4].equals("advanced"))
            difficult=Difficult.advanced;
        else if(params[4].equals("intermediate"))
            difficult=Difficult.intermediate;
        else if(params[4].equals("upper_intermediate"))
            difficult=Difficult.upper_intermediate;
        else
            difficult=Difficult.none;
        rating=0;//Integer.valueOf(params[5]);

        Group=params[6];
        Subgroup=params[7];
        Rubric=params[8];



        /*switch (params[8]){
            case "beginner":
                difficult=Difficult.beginner;
                break;
            case "advanced":
                difficult=Difficult.advanced;
                break;
            case "intermediate":
                difficult=Difficult.intermediate;
                break;
            case "upper_intermediate":
                difficult=Difficult.upper_intermediate;
                break;
            default:
                difficult=Difficult.none;
        }*/



    }
}
