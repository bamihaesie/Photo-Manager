package com.bogdan.photomanager.model;

public class RenameCommand {
	
	String directory;
	String prefixValue;
	String suffixValue;
	
	boolean applyPrefix;
	boolean applySuffix;
	boolean resetNames;
	
	String message;
	
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getPrefixValue() {
		return prefixValue;
	}
	public void setPrefixValue(String prefixValue) {
		this.prefixValue = prefixValue;
	}
	public String getSuffixValue() {
		return suffixValue;
	}
	public void setSuffixValue(String suffixValue) {
		this.suffixValue = suffixValue;
	}
	public boolean isApplyPrefix() {
		return applyPrefix;
	}
	public void setApplyPrefix(boolean applyPrefix) {
		this.applyPrefix = applyPrefix;
	}
	public boolean isApplySuffix() {
		return applySuffix;
	}
	public void setApplySuffix(boolean applySuffix) {
		this.applySuffix = applySuffix;
	}
	public boolean isResetNames() {
		return resetNames;
	}
	public void setResetNames(boolean resetNames) {
		this.resetNames = resetNames;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isValid() {
		
		// directory must be specified
		if (directory == null || directory.isEmpty()) {
			message = "Directory must be specified!";
			return false;
		}
		
		// at least one operation must be specified
		if (applyPrefix == false && applySuffix == false && resetNames == false) {
			message = "At least one operation must be specified!";
			return false;
		}
		
		// is prefix operation, must have prefix value
		if (applyPrefix && (prefixValue == null || prefixValue.isEmpty())) {
			message = "Is prefix operation, must have prefix value!";
			return false;
		}
		
		// is suffix operation, must have suffix value
		if (applySuffix && (suffixValue == null || suffixValue.isEmpty())) {
			message = "Is suffix operation, must have suffix value!";
			return false;
		}
		
		return true;
	}
	
	public String toString() {
		return "RenameCommand \n\t directory= " + directory +
				" \n\t applyPrefix= " + applyPrefix +
				" \n\t prefixValue= " + prefixValue +
				" \n\t applySuffix= " + applySuffix +
				" \n\t suffixValue= " + suffixValue +
				" \n\t resetNames= " + resetNames + " \n\t ";
	}
	
}
