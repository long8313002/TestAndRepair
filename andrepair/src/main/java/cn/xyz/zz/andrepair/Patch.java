/*
 * 
 * Copyright (c) 2015, alipay.com
 *
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
 */

package cn.xyz.zz.andrepair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

class Patch  {
   private final File mFile;
   private List<String> classNames;

   public Patch(File file) throws IOException {
       mFile = file;
       init();
   }

   private void init() throws IOException {
       JarFile jarFile = new JarFile(mFile);
       ZipEntry entry = jarFile.getEntry("fixClassNames.txt");
       InputStream in = jarFile.getInputStream(entry);
       classNames = getClassNames(in);
   }

    public List<String> getClassNames(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> classNames = new ArrayList<>();
        String className;
        while((className = bufferedReader.readLine())!=null){
            classNames.add(className);
        }
        return classNames;
    }

   public File getFile() {
       return mFile;
   }

   public List<String> getClasses() {
       return classNames;
   }
}
