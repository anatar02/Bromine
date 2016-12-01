package util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Utility class for package operations.
 *
 * @author Thibault Helsmoortel
 */
public final class PackageUtil {

    private static final Logger LOGGER = Logger.getLogger(PackageUtil.class);

    /**
     * Returns an array of classes inside a given package.
     *
     * @param packageName the package of which to retrieve the classes from
     * @return the classes inside the given package
     * @throws ClassNotFoundException when a class was not found
     * @throws IOException            when a file could not be found
     */
    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        LOGGER.debug("Fetching all classes inside package: " + packageName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path;
        if (packageName.contains(".")) path = packageName.replace('.', '/');
        else path = packageName;
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Returns a list of classes found in the directory of a package.
     *
     * @param directory   the directory of a package
     * @param packageName the package
     * @return the list of classes found in a package
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
