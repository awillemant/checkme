package io.checkme.tests;

import javax.servlet.ServletContext;

import io.checkme.surv.AbstractCheckme;

public class CheckDB extends AbstractCheckme {

	private CheckDB(String path, ServletContext servletContext) {
		super(path, servletContext);
	}

	@Override
	public boolean isOk() {

		return false;
	}

}
