package org.intuit;

import org.intuit.scoreservice.services.ScoreService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//You are part of a team, building a gaming service. You are responsible for implementing the module that
//keeps track of the all time top scores.

// As players complete a game, the game service will publish the
// player’s score to a topic (you can replace the topic with a flat file).
// file ->

//You are expected to code a service
//that when invoked will return the top 5 scores and the names of the players who attained that score.
//You can use any database that you want.

//Instructions to the candidate:
//1. We are interested in your approach to the problem. It is alright if you don’t have a beautiful PPT
//2. We would like to see working code and at least a few key unit tests.
//Ensure is your code is organized well and is production grade

@SpringBootApplication
@EnableRabbit
@PropertySource("classpath:application.properties")
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}



//daata
// alternate design