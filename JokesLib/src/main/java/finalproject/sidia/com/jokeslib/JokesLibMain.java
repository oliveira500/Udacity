package finalproject.sidia.com.jokeslib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokesLibMain {

    private static JokesLibMain instance = null;

    private JokesLibMain(){}

    private  List<String> jokesList = new ArrayList<String>();

    {
        jokesList.add("Oq a pa falou para outra pa. OPA");
        jokesList.add("Oque o ovo novinho falou pro ovo mais velho? OVO");
        jokesList.add("Oque a mudinha de planta falou pra arvorezinha? Ela nao falou nada , pois e mudinha");
        jokesList.add("O menininho foi na padaria e perguntou ao padeiro : padeiro tem PAES ? ele disse NAES");
        jokesList.add("Sabe o que um bebado mais gosta no Funk? A batida! ");

    }

    public static JokesLibMain getInstance() {

        if(instance == null)
            instance = new JokesLibMain();
        return instance;
    }

    public String getJokesRandom(){

        final Random rand = new Random();

        String randomJoke = jokesList.get(rand.nextInt(jokesList.size()));

        return randomJoke;
    }
}
