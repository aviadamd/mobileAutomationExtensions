package base.mobile.findElements;

import base.staticData.CollectionExtensions;
import base.driversManager.MobileManager;
import base.mobile.enums.DividedList;
import io.appium.java_client.AppiumFluentWait;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static base.staticData.MobileStringsUtilities.hebrewTextLeftToRight;
import static com.aventstack.extentreports.Status.INFO;
import static java.util.Arrays.asList;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Slf4j
public class ElementsSearchExtensions extends MobileManager {

    private Duration timeOut = Duration.ofSeconds(1);
    private Duration pollingEvery = Duration.ofMillis(500);

    public ElementsSearchExtensions overRideWebDriverWait(Duration timeOut, Duration pollingEvery) {
        this.timeOut = timeOut;
        this.pollingEvery = pollingEvery;
        return this;
    }

    private ArrayList<Class<? extends Exception>> errors = new ArrayList<>(asList(
            Exception.class,
            WebDriverException.class,
            TimeoutException.class,
            java.util.concurrent.TimeoutException.class,
            ConcurrentModificationException.class
    ));

    private AppiumFluentWait<WebDriver> appiumFluentWait() {
        AppiumFluentWait<WebDriver> appiumFluentWait = new AppiumFluentWait<>(getDriver());
        appiumFluentWait.withTimeout(this.timeOut).pollingEvery(this.pollingEvery).ignoreAll(this.errors);
        return appiumFluentWait;
    }


    /**
     * streamList
     * @param fatherElements from element to stream pipe line
     * @return Stream<WebElement>
     */
    public Stream<WebElement> streamFindElementsBy(By fatherElements) {
        return this.findElementsBy(fatherElements).stream();
    }

    /**
     * streamList
     * @param fatherElements from element to stream pipe line
     * @return Stream<WebElement>
     */
    public Stream<WebElement> streamFindElementsBy(By fatherElements, By son) {
        return this.findElementSingleToMany(fatherElements, son).stream();
    }

    /**
     * elementList
     * @param elementList fix list
     * @return List<WebElement>
     */
    public Stream<WebElement> fromListToStream(List<WebElement> elementList) {
        return elementList.stream();
    }

    /**
     * fromListToListNestedElements
     * @param elementList fix list
     * @param element nested element to return to list
     * @return List<WebElement>
     */
    public List<WebElement> fromListToListNestedElements(boolean parallel, List<WebElement> elementList, By element) {
        List<WebElement> collector = new ArrayList<>();
        if (parallel) {
            elementList.stream()
                    .parallel()
                    .map(single -> this.appiumFluentWait().until(e -> single.findElements(element)))
                    .forEach(collector::addAll);
        } else {
            elementList.stream()
                    .map(single -> this.appiumFluentWait().until(e -> single.findElements(element)))
                    .forEach(collector::addAll);
        }
        return collector;
    }

    /**
     * streamList
     * @param fatherElements from element to parallel stream pipe line
     * @return Stream<WebElement>
     */
    public Stream<WebElement> streamParallelFindElementsBy(By fatherElements) {
        return this.findElementsBy(fatherElements).parallelStream();
    }

    /**
     * streamList
     * @param fatherElements from element to parallel stream pipe line
     * @return Stream<WebElement>
     */
    public Stream<WebElement> streamParallelFindElementsBy(By fatherElements, By son) {
        return this.findElementSingleToMany(fatherElements, son).parallelStream();
    }

    /**
     * streamParallelListFlux
     * @param fatherElements  from element to flux fromIterable pipe line
     * @return Flux<WebElement>
     */
    public Flux<WebElement> streamParallelListFlux(By fatherElements) {
        return Flux.fromIterable(this.findElementsBy(fatherElements));
    }

    /**
     * streamParallelListFlux
     * @param fatherElements  from element to flux fromIterable pipe line
     * @return Flux<WebElement>
     */
    public Flux<WebElement> streamParallelListFlux(By fatherElements, By son) {
        return Flux.fromIterable(this.findElementSingleToMany(fatherElements, son));
    }

    /**
     * search
     * @param elementList list of web elements
     * @param fullName predicate fix str
     * @param attribute of the element
     * @return single element or null
     */
    public WebElement search(List<WebElement> elementList, String fullName, List<String> attribute) {
        return this.searchElement(elementList, fullName, attribute);
    }

    /**
     * search
     * @param elementList list of web elements
     * @param elementPredicate predicate by your definitions
     * @return single element or null
     */
    public WebElement search(List<WebElement> elementList, Predicate<WebElement> elementPredicate) {
        return this.searchElement(elementList, elementPredicate);
    }

    /**
     * getListToMany
     * @param dividedList
     *  FROM_UPPER_TO_MIDDLE,
     *  FROM_MIDDLE_TO_LOWER,
     *  ALL_COLLECTION
     * @param by element search
     * @return List<WebElement> getListToMany
     */
    public List<WebElement> getListToMany(DividedList dividedList, By by) {
        List<WebElement> elementList = this.findElementsBy(by);
        elementList = new CollectionExtensions().subListList(dividedList, elementList);
        return elementList;
    }

    /**
     * getListToMany
     * @param dividedList
     *  FROM_UPPER_TO_MIDDLE,
     *  FROM_MIDDLE_TO_LOWER,
     *  ALL_COLLECTION
     * @param by element search
     * @param predicate Predicate<WebElement> predicate -> e.getAtt("text").contains("yourSearch")...
     * @return List<WebElement> getListToMany
     */
    public List<WebElement> getListToMany(DividedList dividedList, By by, Predicate<WebElement> predicate) {
        List<WebElement> elementList = this.toManyDistinctBy(by, predicate);
        elementList = new CollectionExtensions().subListList(dividedList, elementList);
        return elementList;
    }

    /**
     * getListFromManyToMany
     * @param dividedList
     *  FROM_UPPER_TO_MIDDLE,
     *  FROM_MIDDLE_TO_LOWER,
     *  ALL_COLLECTION
     * @param by element search
     * @param predicate Predicate<WebElement> predicate -> e.getAtt("text").contains("yourSearch")...
     * @return List<WebElement> getListFromManyToMany
     */
    public List<WebElement> getListFromManyToMany(DividedList dividedList, By by, Predicate<WebElement> predicate) {
        List<WebElement> elementList = this.fromManyToManyDistinct(by, predicate);
        elementList = new CollectionExtensions().subListList(dividedList, elementList);
        return elementList;
    }

    /**
     * getListFromSingleToMany
     * @param dividedList
     *  FROM_UPPER_TO_MIDDLE,
     *  FROM_MIDDLE_TO_LOWER,
     *  ALL_COLLECTION
     * @param fatherElement element search
     * @param elementSon element search
     * @param predicate Predicate<WebElement> predicate -> e.getAtt("text").contains("yourSearch")...
     * @return List<WebElement> getListFromSingleToMany
     */
    public List<WebElement> getListFromSingleToMany(DividedList dividedList, By fatherElement, By elementSon, Predicate<WebElement> predicate) {
        List<WebElement> elementList = this.fromSingleToManyDistinct(fatherElement, elementSon, predicate);
        elementList = new CollectionExtensions().subListList(dividedList, elementList);
        return elementList;
    }

    /**
     * toMany
     * @param by the type to return many
     * @return List<WebElement>
     */
    public List<WebElement> toManyDistinctBy(By by, Predicate<WebElement> predicate) {
        try {
            return this.findElementsBy(by)
                    .stream()
                    .filter(predicate)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * fromSingleToMany
     * @param singleFatherElement from single element to many
     * @param chainElementSon the element son to many
     * @return List<WebElement>
     */
    public List<WebElement> fromSingleToMany(By singleFatherElement, By chainElementSon) {
        try {
            return this.findElementSingleToMany(singleFatherElement, chainElementSon);
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * fromSingleToMany
     * @param singleFatherElement from single element to many
     * @param chainElementSon the element son to many
     * @return List<WebElement>
     */
    public List<WebElement> fromSingleToManyDistinct(By singleFatherElement, By chainElementSon, Predicate<WebElement> predicate) {
        try {
            return this.findElementSingleToMany(singleFatherElement, chainElementSon)
                    .stream()
                    .filter(predicate)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * fromManyToMany
     * @param fatherElements from many to predicate result
     * @param predicate condition
     * @return List<WebElement>
     */
    public List<WebElement> fromManyToManyDistinct(By fatherElements, Predicate<WebElement> predicate) {
        try {
            return this.findElementsBy(fatherElements)
                    .stream()
                    .filter(predicate)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * fromManyToManyZipTwoLists
     * @param firstList List<WebElement> firstList
     * @param secondList List<WebElement> secondList
     * @return fix list
     */
    public List<WebElement> fromManyToManyZipTwoLists(By firstList, By secondList) {
        List<WebElement> collector = new ArrayList<>();
        try {
            collector.addAll(this.findElementsBy(firstList));
            collector.addAll(this.findElementsBy(secondList));
            return collector;
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return collector;
        }
    }

    /**
     * findElementBy
     * @param by element
     * @param <T> any type
     * @return WebElement
     */
    public<T extends WebElement> T findElementBy(By by) {
        try {
            return this.appiumFluentWait().until(e -> e.findElement(by));
        } catch (Exception e) {
            log.debug("cannot find element " + e.getMessage());
            return null;
        }
    }

    /**
     * findElementBy
     * @param by element
     * @param <T> any type
     * @return List<WebElement>
     */
    public<T extends WebElement> List<WebElement> findElementsBy(By by) {
        try {
            return this.appiumFluentWait().until(e -> e.findElements(by));
        } catch (Exception e) {
            log.debug("cannot find elements " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * findElementBy
     * @param first to second
     * @param second to third
     * @param <T> any type
     * @return WebElement
     */
    public<T extends WebElement> T findElementBy(By first, By second) {
        try {
            return this.appiumFluentWait()
                    .until(e -> e.findElement(first)
                    .findElement(second));
        } catch (Exception e) {
            log.debug("cannot find element " + e.getMessage());
            return null;
        }
    }

    /**
     * findElementBy
     * @param first to second
     * @param second to third
     * @param third final result
     * @param <T> any type
     * @return WebElement
     */
    public<T extends WebElement> T findElementBy(By first, By second, By third) {
        try {
            return this.appiumFluentWait()
                    .until(e -> e.findElement(first)
                            .findElement(second)
                            .findElement(third));
        } catch (Exception e) {
            log.debug("cannot find element " + e.getMessage());
            return null;
        }
    }

    /**
     * fromFatherToNestedList
     * @param father from by
     * @param son to elements
     * @return List<WebElement>
     */
    public List<WebElement> fromManyToNestedMany(By father, By son) {
        try {
            return this.appiumFluentWait()
                    .until(elementFather -> elementFather.findElements(father))
                    .stream()
                    .flatMap(sons -> sons.findElements(son).stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.debug("cannot find elements " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * findElementBy
     * @param father single element
     * @param childes to elements
     * @param <T> any type
     * @return List<WebElement>
     */
    public<T extends WebElement> List<WebElement> findElementSingleToMany(By father, By childes) {
        try {
            return this.appiumFluentWait().until(e -> e.findElement(father).findElements(childes));
        } catch (Exception e) {
            log.debug("cannot find elements " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * findElementSingleToSingleToMany
     * @param first to second
     * @param second to third
     * @param third final result
     * @param <T> any type
     * @return WebElement
     */
    public<T extends WebElement> List<WebElement> findElementSingleToSingleToMany(By first, By second, By third) {
        try {
            return this.appiumFluentWait()
                    .until(e -> e.findElement(first)
                    .findElement(second)
                    .findElements(third));
        } catch (Exception e) {
            log.debug("cannot find elements " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private WebElement searchElement(List<WebElement> elementList, Predicate<WebElement> elementPredicate) {
        AtomicReference<Boolean> isFindEle = new AtomicReference<>(false);
        AtomicReference<WebElement> webButton = new AtomicReference<>(null);

        try {
            if (!elementList.isEmpty()) {
                try {
                    elementList.parallelStream()
                            .filter(elementPredicate)
                            .forEach(eleButton -> {
                                webButton.set(eleButton);
                                isFindEle.set(true);
                            });
                } catch (ConcurrentModificationException | NullPointerException cEx) {
                    log.error("searchElement inner loop run error ");
                } catch (Exception ex) {
                    log.error("searchElement inner loop general error ");
                }
            } else {
                log.error("searchElement is empty ");
            }
        } catch (Exception ex) {
            log.error("searchElement outer general error ");
        }

        return webButton.get();
    }

    private WebElement searchElement(List<WebElement> elementList, String fullName, List<String> attrs) {
        AtomicReference<Boolean> isFindEle = new AtomicReference<>(false);
        AtomicReference<WebElement> webButton = new AtomicReference<>(null);

        try {
            if (!elementList.isEmpty()) {
                try {
                    elementList.parallelStream()
                            .filter(element -> this.isTextEqualsTo(element, fullName, attrs))
                            .forEach(eleButton -> {
                                webButton.set(eleButton);
                                isFindEle.set(true);
                            });
                } catch (ConcurrentModificationException | NullPointerException cEx) {
                    log.error("searchElement inner loop run error ");
                } catch (Exception ex) {
                    log.error("searchElement inner loop general error ");
                }
            } else {
                log.error("searchElement is empty ");
            }
        } catch (Exception ex) {
            log.error("searchElement outer general error ");
        }

        return webButton.get();
    }

    /**
     * isTextEqualsTo
     * @param element
     * @param fullName
     * @param attribute
     * @return
     */
    private boolean isTextEqualsTo(WebElement element, String fullName, List<String> attribute) {
        return new VerificationsTextsExtensions().isTextEquals(
                this.getValueEle(element, attribute),
                fullName,
                INFO
        );
    }

    /**
     * this validate that from list of more than 50 items that the element value is == isEnabled for click actions
     * @param element the WebElement
     * @return MobileStringsUtilities.hebrewTextLeftToRight(strTextElement);
     */
    public String getValueEle(WebElement element) {
        String strTextElement = "";
        try {
            this.appiumFluentWait().until(elementToBeClickable(element)).isEnabled();
            strTextElement = element.getText();
        } catch (ConcurrentModificationException concurrentEx) {
            log.debug("ConcurrentModificationException continue search ");
        } catch (Exception exception) {
            log.debug("Exception continue search ");
        }

        return hebrewTextLeftToRight(strTextElement);
    }

    /**
     * this validate that from list of more than 50 items that the element value is == isEnabled for click actions
     * @param element the WebElement
     * @return MobileStringsUtilities.hebrewTextLeftToRight(strTextElement);
     */
    public String getValueEle(WebElement element, List<String> attribute) {
        String strTextElement = "";
        try {

            this.appiumFluentWait().until(elementToBeClickable(element)).isEnabled();
            strTextElement = isAndroidClient()
                    ? element.getAttribute(attribute.get(0))
                    : element.getAttribute(attribute.get(1));

        } catch (ConcurrentModificationException concurrentEx) {
            log.debug("ConcurrentModificationException continue search ");
        } catch (Exception exception) {
            log.debug("Exception continue search ");
        }

        return hebrewTextLeftToRight(strTextElement);
    }

}
