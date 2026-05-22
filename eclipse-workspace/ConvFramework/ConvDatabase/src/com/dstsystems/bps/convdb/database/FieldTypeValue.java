package com.dstsystems.bps.convdb.database;

public class FieldTypeValue {
	
	/**
	 * @param _dataValue
	 * @param _fieldType
	 * @param _fieldPos
	 */
	public FieldTypeValue(Object _dataValue, int _fieldType, int _fieldPos) {
		this._dataValue = _dataValue;
		this._fieldType = _fieldType;
		this._fieldPos = _fieldPos;
	}
	
	public Object get_dataValue() {
		return _dataValue;
	}
	public void set_dataValue(Object _dataValue) {
		this._dataValue = _dataValue;
	}
	public int get_fieldType() {
		return _fieldType;
	}
	public void set_fieldType(int _fieldType) {
		this._fieldType = _fieldType;
	}
	public int get_fieldPos() {
		return _fieldPos;
	}
	public void set_fieldPos(int _fieldPos) {
		this._fieldPos = _fieldPos;
	}
	
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FieldTypeValue [");
		if (_dataValue != null) {
			builder.append("_dataValue=");
			builder.append(_dataValue);
			builder.append(", ");
		}
		builder.append("_fieldType=");
		builder.append(_fieldType);
		builder.append(", _fieldPos=");
		builder.append(_fieldPos);
		builder.append("]");
		return builder.toString();
	}
	
	private Object _dataValue;
	private int _fieldType;
	private int _fieldPos;
	
}
