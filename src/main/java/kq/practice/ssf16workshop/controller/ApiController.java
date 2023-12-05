package kq.practice.ssf16workshop.controller;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import kq.practice.ssf16workshop.Repository.BoardgameRepo;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiController {

    @Autowired
    BoardgameRepo repo;

    @PostMapping(path = "/boardgame")
    public ResponseEntity<String> postBoardgame(@RequestBody String payload) {

        JsonReader jr = Json.createReader(new StringReader(payload));
        JsonArray data = jr.readArray();

        List<JsonObject> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {

            String gid = repo.save(data.get(i).asJsonObject());

            JsonObject resp = Json.createObjectBuilder()
                    .add("insert_count", 1)
                    .add("id", gid)
                    .build();

            list.add(resp);
        }

        JsonArray array = Json.createArrayBuilder(list).build();

        return ResponseEntity.status(201)
                .body(array.toString());
    }

    @GetMapping(path = "boardgame/{gid}")
    public ResponseEntity<String> getBoardgame(@PathVariable("gid") String id) {

        if (repo.exist(id)) {
            Map<String, String> map = repo.get(id);

            JsonObject game = Json.createObjectBuilder()
                    .add("name", map.get("name"))
                    .add("year", map.get("year"))
                    .add("ranking", map.get("ranking"))
                    .add("users_rated", map.get("users_rated"))
                    .add("url", map.get("url"))
                    .add("image", map.get("image"))
                    .build();

            return ResponseEntity.status(200)
                    .body(game.toString());
        }

        return ResponseEntity.status(404)
                .body("Boardgame not found");
    }

    @PutMapping(path = "boardgame/{gid}")
    public ResponseEntity<String> putBoardgame(@PathVariable("gid") String id, @RequestBody String payload,
            @RequestParam(name = "upsert", defaultValue =  "false") Boolean upsert) {

        if (repo.exist(id)) {
            JsonReader jr = Json.createReader(new StringReader(payload));
            JsonObject data = jr.readObject();

            repo.save(data);

            JsonObject resp = Json.createObjectBuilder()
                    .add("update_count", 1)
                    .add("id", id)
                    .build();

            return ResponseEntity.status(200)
                    .body(resp.toString());
        } else if (upsert == true) {

            JsonReader jr = Json.createReader(new StringReader(payload));
            JsonObject data = jr.readObject();

            String gid = repo.save(data);

            JsonObject resp = Json.createObjectBuilder()
                    .add("insert_count", 1)
                    .add("id", gid)
                    .build();

            return ResponseEntity.status(200)
                    .body(resp.toString());
        } else {

            return ResponseEntity.status(404)
                    .body("Boardgame not found");
        }
    }
}
