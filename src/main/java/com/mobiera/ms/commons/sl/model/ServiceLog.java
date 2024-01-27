package com.mobiera.ms.commons.sl.model;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;



/**
 * The persistent class for the cp_spool database table.
 * 
 */
@Entity
@Table(name="service_log")
@DynamicUpdate
@DynamicInsert


@NamedQueries({
	
})

public class ServiceLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="SL_ID_GENERATOR", sequenceName="HIBERNATE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SL_ID_GENERATOR")
	private Long id;
	@Column(columnDefinition="timestamptz")
	private Instant ts;
	@Column(columnDefinition="timestamptz")
	private Instant persistedTs;
	@Column(columnDefinition="text")
	private String module;
	@Column(columnDefinition="text")
	private String entity;
	@Column(columnDefinition="text")
	private String data1;
	@Column(columnDefinition="text")
	private String data2;
	@Column(columnDefinition="text")
	private String data3;
	@Column(columnDefinition="text")
	private String data4;
	@Column(columnDefinition="text")
	private String data5;
	@Column(columnDefinition="text")
	private String data6;
	@Column(columnDefinition="text")
	private String data7;
	@Column(columnDefinition="text")
	private String data8;
	@Column(columnDefinition="text")
	private String data9;
	@Column(columnDefinition="text")
	private String dataA;
	@Column(name="userid", columnDefinition="text")
	private String user;
	@Column(columnDefinition="text")
	private String instance;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Instant getTs() {
		return ts;
	}
	public void setTs(Instant ts) {
		this.ts = ts;
	}
	
	public String getData1() {
		return data1;
	}
	public void setData1(String data1) {
		this.data1 = data1;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	public String getData4() {
		return data4;
	}
	public void setData4(String data4) {
		this.data4 = data4;
	}
	public String getData5() {
		return data5;
	}
	public void setData5(String data5) {
		this.data5 = data5;
	}
	public String getData6() {
		return data6;
	}
	public void setData6(String data6) {
		this.data6 = data6;
	}
	public String getData7() {
		return data7;
	}
	public void setData7(String data7) {
		this.data7 = data7;
	}
	public String getData8() {
		return data8;
	}
	public void setData8(String data8) {
		this.data8 = data8;
	}
	public Instant getPersistedTs() {
		return persistedTs;
	}
	public void setPersistedTs(Instant persistedTs) {
		this.persistedTs = persistedTs;
	}
	public String getData9() {
		return data9;
	}
	public void setData9(String data9) {
		this.data9 = data9;
	}
	public String getDataA() {
		return dataA;
	}
	public void setDataA(String dataA) {
		this.dataA = dataA;
	}
	
	
	
}