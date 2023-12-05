package kq.practice.ssf16workshop.Repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class BoardgameRepo {

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    public String save(JsonObject obj) {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        String gid = obj.get("gid").toString();
        hashOps.put(gid, "name", obj.getString("name"));
        hashOps.put(gid, "year", obj.get("year").toString());
        hashOps.put(gid, "ranking", obj.get("ranking").toString());
        hashOps.put(gid, "users_rated", obj.get("users_rated").toString());
        hashOps.put(gid, "url", obj.getString("url"));
        hashOps.put(gid, "image", obj.getString("image"));

        return gid;
    }

    public Boolean exist(String id) {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        if (hashOps.keys(id).size() > 0) {
            return true;
        }

        return false;
    }

    public Map<String, String> get(String id) {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        Map<String, String> map = new HashMap<>();
        map.put("name", hashOps.get(id, "name"));
        map.put("year", hashOps.get(id, "year"));
        map.put("ranking", hashOps.get(id, "ranking"));
        map.put("users_rated", hashOps.get(id, "users_rated"));
        map.put("url", hashOps.get(id, "url"));
        map.put("image", hashOps.get(id, "image"));

        return map;
    }
}
