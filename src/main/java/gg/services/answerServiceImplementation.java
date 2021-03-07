package gg.services;

import org.springframework.stereotype.Repository;

import java.util.Random;
@Repository
public class answerServiceImplementation implements answerService{


@Override
    public String generateAnswer(){

        Random rand = new Random();
        String key = "";
        while(key.length()<4){
            int temp = rand.nextInt(10);
            if(!(key.contains(String.valueOf(temp)))){
                key += String.valueOf(temp);
            }
        }
        return key;

    }
}
