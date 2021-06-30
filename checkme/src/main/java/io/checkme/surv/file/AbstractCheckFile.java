package io.checkme.surv.file;

import io.checkme.surv.AbstractCheckme;
import org.slf4j.Logger;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by a527968 on 06/02/2017.
 */
abstract class AbstractCheckFile extends AbstractCheckme {

    public AbstractCheckFile(String path, ServletContext servletContext) {
        super(path, servletContext);
    }

    abstract String getFiletoSurv();

    abstract Logger getLogger();

    @Override
    public boolean isOk() {
        String absolutePath = getFiletoSurv();
        getLogger().debug("AbsolutePath is : {}", absolutePath);
        Path path = Paths.get(absolutePath);

        return path.toFile().exists();
    }


}
