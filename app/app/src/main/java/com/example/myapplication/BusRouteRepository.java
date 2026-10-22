package com.example.myapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusRouteRepository {
    private static final List<BusRoute> ROUTES = new ArrayList<>();

    static {
        ROUTES.add(new BusRoute(
                "BUS_01",
                "BUS 12",
                "Route 01",
                "City & Metro Express",
                "Bhawarkua Metro ⇄ Bhanwarkuan ⇄ NMIMS Main Gate",
                "Rajesh Kumar",
                "9876543210",
                "Bhawarkua Metro Stn",
                "NMIMS Main Gate",
                22.7196,
                75.8577
        ));

        ROUTES.add(new BusRoute(
                "BUS_02",
                "BUS 08",
                "Route 02",
                "Hostel & Campus Shuttle",
                "Hostel Blocks A-C ⇄ Annapurna ⇄ Tech Hub",
                "Vikram Singh",
                "9823456789",
                "Boys Hostel Block B",
                "Central Cafeteria",
                22.7240,
                75.8650
        ));

        ROUTES.add(new BusRoute(
                "BUS_03",
                "BUS 05",
                "Route 03",
                "Tech Park & Labs Loop",
                "Super Corridor ⇄ IT Park ⇄ Engineering Block",
                "Mahesh Sharma",
                "9712345678",
                "Super Corridor Crossing",
                "Computer Science Labs",
                22.7530,
                75.8937
        ));
    }

    public static List<BusRoute> getRoutes() {
        return Collections.unmodifiableList(ROUTES);
    }

    public static BusRoute getRouteById(String busId) {
        for (BusRoute route : ROUTES) {
            if (route.busId.equals(busId)) {
                return route;
            }
        }
        return ROUTES.get(0);
    }
}
