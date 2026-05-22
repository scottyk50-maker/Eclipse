package com.dstsystems.bps.cfw.pub.interfaces;

public interface IConfig {

	public String get_license();
	public void set_license(final String license);
	public int get_threadCount();
	public void set_threadCount(final int threadCount);
	public long get_providerSleep();
	public void set_providerSleep(final long providerSleep);
	public String get_providerClass();
	public void set_providerClass(final String providerClass);
	public String get_consumerClass();
	public void set_consumerClass(final String consumerClass);
	public boolean is_endNoRecFound();
	public void set_endNoRecFound(final boolean endNoRecFound);
}
