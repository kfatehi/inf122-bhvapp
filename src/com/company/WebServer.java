package com.company;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Stream;

import static spark.Spark.*;

public class WebServer {
    public static void start(KeyValueStore db) {
        staticFileLocation("/public");

        get("/children/:childName/tokens", (req, res) -> {
            String childName = req.params(":childName");
            LocalDate start = LocalDate.parse(req.queryParams("start"));
            LocalDate end = LocalDate.parse(req.queryParams("end"));
            Child child = Child.load(db, childName);
            if (child != null) {
                System.out.println("1");
                JsonArray days = new JsonArray();
                System.out.println("2");
                Stream.iterate(start, date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(start, end) + 1)
                        .forEach(localDate -> {
                            System.out.println("3");
                            JsonObject day = new JsonObject();
                            day.put("date", localDate.toString());
                            System.out.println("3.2");
                            day.put("count", child.countTokensOnDay(localDate));
                            System.out.println("4");
                            days.add(day.toJson());
                        });
                res.type("application/json");
                res.status(200);
                return days.toString();
            } else {
                res.status(404);
                return "not found";
            }
        });

        System.out.println("Spark listening on port 4567");

    }
}