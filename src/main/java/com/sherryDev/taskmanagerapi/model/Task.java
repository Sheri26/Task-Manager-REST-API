package com.sherryDev.taskmanagerapi.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

//import lombok.Getter;
//import lombok.Setter;


@Entity
@Table(name = "Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tId;

    @Column(name = "tName", nullable = false)
    private String tName;

    @Column(name = "tDescription")
    private String tDescription;

    @Column(name = "tDateCreated")
    @CreationTimestamp
    private LocalDate tDateCreated;

    public Task(String tName, String tDescription) {
        this.tName = tName;
        this.tDescription = tDescription;
    }

    public Task(int tId, String tName, String tDescription, LocalDate tDateCreated) {
        this.tId = tId;
        this.tName = tName;
        this.tDescription = tDescription;
        this.tDateCreated = tDateCreated;
    }

    public Task() {
    }

    public int getTId() {
        return this.tId;
    }

    public String getTName() {
        return this.tName;
    }

    public String getTDescription() {
        return this.tDescription;
    }

    public LocalDate getTDateCreated() {
        return this.tDateCreated;
    }

    public void setTId(int tId) {
        this.tId = tId;
    }

    public void setTName(String tName) {
        this.tName = tName;
    }

    public void setTDescription(String tDescription) {
        this.tDescription = tDescription;
    }

    public void setTDateCreated(LocalDate tDateCreated) {
        this.tDateCreated = tDateCreated;
    }

    public String toString() {
        return "Task(tId=" + this.getTId() + ", tName=" + this.getTName() + ", tDescription=" + this.getTDescription() + ", tDateCreated=" + this.getTDateCreated() + ")";
    }

//    	public int gettId() {
//		return tId;
//	}
//
//	public void settId(int tId) {
//		this.tId = tId;
//	}
//
//	public String gettName() {
//		return tName;
//	}
//
//	public void settName(String tName) {
//		this.tName = tName;
//	}
//
//	public String gettDescription() {
//		return tDescription;
//	}
//
//	public void settDescription(String tDescription) {
//		this.tDescription = tDescription;
//	}
//
//	public LocalDate gettDateCreated() {
//		return tDateCreated;
//	}
//
//	public void settDateCreated(LocalDate tDateCreated) {
//		this.tDateCreated = tDateCreated;
//	}
//
//    @Column
//    private boolean tComplete;


}
