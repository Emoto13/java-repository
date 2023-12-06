package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI, EscapeRoomPortalAPI {
    private Team[] teams;
    private int maxCapacity;
    private int totalRooms;
    private EscapeRoom[] rooms;

    public EscapeRoomPlatform(Team[] teams, int maxCapacity) {
        this.teams = teams;
        this.maxCapacity = maxCapacity;
        this.totalRooms = 0;
        this.rooms = new EscapeRoom[this.maxCapacity];
    }

    private int getFreeRooms() {
        int totalRooms = 0;
        for (int i = 0; i < this.maxCapacity; i++) {
            if (this.rooms[i] == null) totalRooms++;
        }
        return totalRooms;
    }

    private Team getTeam(String teamName) {
        for (Team team : teams) {
            if (team != null && team.getName().equals(teamName)) return team;
        }
        return null;
    }

    private int getEscapeRoomIndex(String roomName) throws RoomNotFoundException{
        for (int i = 0; i < rooms.length; i++) {
            EscapeRoom room = rooms[i];
            if (room != null && room.getName().equals(roomName)) {
                return i;
            }
        }

        throw new RoomNotFoundException();
    }

    private EscapeRoom getEscapeRoom(String roomName) {
        for (EscapeRoom room : rooms) {
            if (room != null && room.getName().equals(roomName)) {
                return room;
            }
        }

        return null;
    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {
        EscapeRoom room = getEscapeRoom(roomName);
        if (room == null) {
            throw new RoomNotFoundException();
        }
        return room;
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {
        EscapeRoom room = this.getEscapeRoomByName(roomName);
        room.addReview(review);
    }

    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {
        EscapeRoom room = this.getEscapeRoomByName(roomName);
        return room.getReviews();
    }

    @Override
    public Team getTopTeamByRating() {
        double maxRating = 0.0;
        Team topTeam = null;
        for (Team team : this.teams) {
            if (team != null && team.getRating() > maxRating) {
                maxRating = team.getRating();
                topTeam = team;
            }
        }

        return topTeam;
    }

    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException, PlatformCapacityExceededException {
        if (room == null) { 
            throw new IllegalArgumentException("null room");
        } 

        EscapeRoom escapeRoom = this.getEscapeRoom(room.getName());
        if (escapeRoom != null) {
            throw new RoomAlreadyExistsException();
        }

        if (this.totalRooms >= this.rooms.length) {
            for (int i = 0; i < this.rooms.length; i++) {
                if (this.rooms[i] == null) {
                    this.rooms[i] = room;
                    return;
                }
            }
            throw new PlatformCapacityExceededException();
        }

        this.rooms[this.totalRooms] = room;
        this.totalRooms++;
    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException, IllegalArgumentException {
        if (roomName == null || roomName.isEmpty() || roomName.isBlank()) throw new IllegalArgumentException("empty room name");

        int escapeRoomIndex = this.getEscapeRoomIndex(roomName);
        this.rooms[escapeRoomIndex] = null;
    }

    @Override
    public EscapeRoom[] getAllEscapeRooms() {
        int freeRooms = this.getFreeRooms();
        if (freeRooms == 0) return this.rooms;

        EscapeRoom[] newRooms = new EscapeRoom[this.maxCapacity - freeRooms];
        int idx = 0;
        for (int i = 0; i < this.maxCapacity; i++) {
            if (this.rooms[i] != null) {
                newRooms[idx] = this.rooms[i];
                idx++;
            }
        }
        return newRooms;
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime)
            throws RoomNotFoundException, TeamNotFoundException {
        if (roomName == null || roomName.isEmpty() || roomName.isBlank()) throw new IllegalArgumentException("empty room name");
        if (teamName == null || teamName.isEmpty() || teamName.isBlank()) throw new IllegalArgumentException("empty team name");
        
        EscapeRoom room = this.getEscapeRoom(roomName);
        if (room == null) throw new RoomNotFoundException();
        if (escapeTime <= 0 || escapeTime > room.getMaxTimeToEscape()) throw new IllegalArgumentException("invalid escape time");

        Team team = this.getTeam(teamName);
        if (team == null) throw new TeamNotFoundException();

        int score = room.getDifficulty().getRank() + (room.getMaxTimeToEscape() - escapeTime) / 10;
        if (score == 6) score = 5; 
        team.updateRating(score);
    }

}
