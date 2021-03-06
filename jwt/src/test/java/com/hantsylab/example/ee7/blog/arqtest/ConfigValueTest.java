package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.config.ConfigValue;
import com.hantsylab.example.ee7.blog.config.PropertiesFileLoader;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ConfigValueTest {

    @Deployment(name = "test")
    public static Archive<?> createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addPackage(ConfigValue.class.getPackage())
            .addAsResource("test-config.properties", "config.properties")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        // System.out.println(archive.toString(true));
        return archive;
    }

    @Inject
    @ConfigValue("test.string")
    String testString;

    @Inject
    @ConfigValue("test.int")
    Integer testInt;

    @Inject
    PropertiesFileLoader loader;

    @Test
    public void testInjectStringValue() {
        assertTrue("test".equals(testString));
    }

    @Test
    public void testInjectIntValue() {
        assertTrue(1 == testInt);
    }

    @Test
    public void testLoadedProperties() {
        String _test = loader.getValue("test.string").toString();
        int _int = Integer.parseInt(loader.getValue("test.int").toString());
        assertTrue("test".equals(_test));
        assertTrue(1 == _int);
    }

}
