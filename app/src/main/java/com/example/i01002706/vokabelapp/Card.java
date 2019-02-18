package com.example.i01002706.vokabelapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Cardset.class, parentColumns = "id", childColumns = "cardset_id"))
public class Card {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String frage;
    private String antwort;

    private int cardset_id;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public int getCardset_id() {
        return cardset_id;
    }

    public void setCardset_id(int cardset_id) {
        this.cardset_id = cardset_id;
    }


}
