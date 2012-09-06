package com.chihuo.bussiness;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.chihuo.util.JaxbDateSerializer;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AllDomain implements java.io.Serializable {
	@XmlElement
	private List<Category> addCategories;
	@XmlElement
	private List<Category> updateCategories;
	@XmlElement
	private List<Integer> deleteCategories;
	@XmlElement
	private List<Recipe> addRecipes;
	@XmlElement
	private List<Recipe> updateRecipes;
	@XmlElement
	private List<Integer> deleteRecipes;
	@XmlElement
	private List<Desk> addDesks;
	@XmlElement
	private List<Desk> updateDesks;
	@XmlElement
	private List<Integer> deleteDesks;
	@XmlElement
	private List<DeskType> addDeskTypes;
	@XmlElement
	private List<Desk> updateDeskTypes;
	@XmlElement
	private List<Integer> deleteDeskTypes;
	
	
	
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	private Date date;

	public List<Category> getAddCategories() {
		return addCategories;
	}

	public void setAddCategories(List<Category> addCategories) {
		this.addCategories = addCategories;
	}

	public List<Category> getUpdateCategories() {
		return updateCategories;
	}

	public void setUpdateCategories(List<Category> updateCategories) {
		this.updateCategories = updateCategories;
	}

	public List<Integer> getDeleteCategories() {
		return deleteCategories;
	}

	public void setDeleteCategories(List<Integer> deleteCategories) {
		this.deleteCategories = deleteCategories;
	}

	public List<Recipe> getAddRcipes() {
		return addRecipes;
	}

	public void setAddRcipes(List<Recipe> addrRcipes) {
		this.addRecipes = addrRcipes;
	}

	public List<Recipe> getUpdateRecipes() {
		return updateRecipes;
	}

	public void setUpdateRecipes(List<Recipe> updateRecipes) {
		this.updateRecipes = updateRecipes;
	}

	public List<Integer> getDeleteRecipes() {
		return deleteRecipes;
	}

	public void setDeleteRecipes(List<Integer> deleteRecipes) {
		this.deleteRecipes = deleteRecipes;
	}

	public List<Desk> getAddDesks() {
		return addDesks;
	}

	public void setAddDesks(List<Desk> addDesks) {
		this.addDesks = addDesks;
	}

	public List<Desk> getUpdateDesks() {
		return updateDesks;
	}

	public void setUpdateDesks(List<Desk> updateDesks) {
		this.updateDesks = updateDesks;
	}

	public List<Integer> getDeleteDesks() {
		return deleteDesks;
	}

	public void setDeleteDesks(List<Integer> deleteDesks) {
		this.deleteDesks = deleteDesks;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AllDomain(List<Category> addCategories,
			List<Category> updateCategories, List<Integer> deleteCategories,
			List<Recipe> addrRcipes, List<Recipe> updateRecipes,
			List<Integer> deleteRecipes, List<Desk> addDesks,
			List<Desk> updateDesks, List<Integer> deleteDesks, Date date) {
		super();
		this.addCategories = addCategories;
		this.updateCategories = updateCategories;
		this.deleteCategories = deleteCategories;
		this.addRecipes = addrRcipes;
		this.updateRecipes = updateRecipes;
		this.deleteRecipes = deleteRecipes;
		this.addDesks = addDesks;
		this.updateDesks = updateDesks;
		this.deleteDesks = deleteDesks;
		this.date = date;
	}

	public AllDomain() {
	}

	public List<DeskType> getAddDeskTypes() {
		return addDeskTypes;
	}

	public void setAddDeskTypes(List<DeskType> addDeskTypes) {
		this.addDeskTypes = addDeskTypes;
	}

	public List<Desk> getUpdateDeskTypes() {
		return updateDeskTypes;
	}

	public void setUpdateDeskTypes(List<Desk> updateDeskTypes) {
		this.updateDeskTypes = updateDeskTypes;
	}

	public List<Integer> getDeleteDeskTypes() {
		return deleteDeskTypes;
	}

	public void setDeleteDeskTypes(List<Integer> deleteDeskTypes) {
		this.deleteDeskTypes = deleteDeskTypes;
	}

	
}
