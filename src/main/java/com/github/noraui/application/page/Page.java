/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.application.page;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import com.github.noraui.cucumber.injector.NoraUiInjector;
import com.github.noraui.exception.Callbacks.Callback;
import com.github.noraui.exception.TechnicalException;
import com.github.noraui.log.annotation.Loggable;
import com.github.noraui.utils.Context;
import com.github.noraui.utils.Messages;

@Loggable
public abstract class Page implements IPage {

    static Logger log;

    public static final String UNABLE_TO_RETRIEVE_PAGE = "UNABLE_TO_RETRIEVE_PAGE";

    public static final String UNABLE_TO_RETRIEVE_PAGE_ELEMENT = "UNABLE_TO_RETRIEVE_PAGE_ELEMENT";

    public static final String ERROR_DURING_PAGE_ELEMENT_LOOKUP = "ERROR_DURING_PAGE_ELEMENT_LOOKUP";

    private static String pagesPackage = Page.class.getPackage().getName() + '.';

    protected Page motherPage = null;

    protected String pageKey;

    protected String application;

    protected Callback callBack;

    protected Page() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Callback getCallBack() {
        return callBack;
    }

    /**
     * Finds a Page by its name (not the full qualified name).
     * Create a instance or use instance if already exist (com.google.inject.@Singleton).
     *
     * @param className
     *            The name of the class to find. Full qualified name is not required.
     *            Ex: 'MyPage' or 'mypackageinpages.MyPage'
     * @return
     *         A Page instance
     * @throws TechnicalException
     *             if ClassNotFoundException in getInstance() of Page.
     */
    public static Page getInstance(String className) throws TechnicalException {
        try {
            return (Page) NoraUiInjector.getNoraUiInjectorSource().getInstance(Class.forName(pagesPackage + className));
        } catch (ClassNotFoundException e) {
            throw new TechnicalException(Messages.format(Messages.getMessage(UNABLE_TO_RETRIEVE_PAGE), className), e);
        }
    }

    /**
     * Sets the Page main package used to find pages by their class name.
     *
     * @param packageName
     *            The new Page package name
     */
    public static void setPageMainPackage(String packageName) {
        pagesPackage = packageName;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws TechnicalException
     */
    @Override
    public PageElement getPageElementByKey(String key) throws TechnicalException {
        PageElement p;
        try {
            for (final Field f : getClass().getDeclaredFields()) {
                if (PageElement.class.equals(f.getType())) {
                    p = (PageElement) f.get(this);
                    if (key.equals(p.getKey())) {
                        return p;
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new TechnicalException(Messages.format(Messages.getMessage(ERROR_DURING_PAGE_ELEMENT_LOOKUP), key), e);
        }
        return new PageElement(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return Context.getDriver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageKey() {
        return pageKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplication() {
        return application;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page getMotherPage() {
        return motherPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMotherPage(Page motherPage) {
        this.motherPage = motherPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.application + '.' + this.pageKey;
    }

    public class PageElement implements IPageElement {

        private String key = "";
        private String label = "";

        /**
         * The PageElement is the object that makes the link between the selector of the ini file and the use java side.
         * 
         * @param key
         *            The key that is used to find the right selector in the ini file.
         */
        public PageElement(String key) {
            this.key = key;
        }

        /**
         * The PageElement is the object that makes the link between the selector of the ini file and the use java side.
         * 
         * @param key
         *            The key that is used to find the right selector in the ini file.
         * @param label
         *            The wording that is displayed in the report.
         */
        public PageElement(String key, String label) {
            this.key = key;
            this.label = label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getKey() {
            return this.key;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLabel() {
            return this.label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Page getPage() {
            return Page.this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            String ret = getPage().toString();
            if ("".equals(label)) {
                ret += key;
            } else {
                ret += '.' + label;
            }
            return ret;
        }

    }
}
