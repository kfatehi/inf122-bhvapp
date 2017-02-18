package com.company;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static spark.Spark.*;

public class WebInterface implements UserInterface {
    @Override
    public void start() {
        KeyValueStore db = Main.getDatabase();
        staticFileLocation("public");

        get("/children/:childName/chart/:start/:end", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", req.params(":childName"));
            attributes.put("start", req.params(":start"));
            attributes.put("end", req.params(":end"));
            return new ModelAndView(attributes, "chart.ftl");
        }, new FreeMarkerEngine());

        get("/children/:childName/tokens/:start/:end", (req, res) -> {
            String childName = req.params(":childName");
            String startStr = req.params(":start");
            String endStr = req.params(":end");
            LocalDate start = LocalDate.parse(startStr);
            LocalDate end = LocalDate.parse(endStr);
            Child child = Child.load(db, childName);
            if (child != null) {
                JsonArray days = new JsonArray();
                Stream.iterate(start, date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(start, end) + 1)
                        .forEach(localDate -> {
                            JsonObject day = new JsonObject();
                            day.put("date", localDate.toString());
                            day.put("count", child.countTokensOnDay(localDate));
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
    }
}