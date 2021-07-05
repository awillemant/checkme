package io.checkme.surv.file;

/*-
 * #%L
 * checkme
 * %%
 * Copyright (C) 2013 - 2021 Apa
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
