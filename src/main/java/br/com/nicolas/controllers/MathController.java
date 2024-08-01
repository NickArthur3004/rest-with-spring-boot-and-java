package br.com.nicolas.controllers;

import br.com.nicolas.converters.NumberConverter;
import br.com.nicolas.exceptions.UnsupportedMathOperationException;
import br.com.nicolas.math.SimpleMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {

    @Autowired
    private NumberConverter converter;
    @Autowired
    private SimpleMath math;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!converter.isNumeric(numberOne) || !converter.isNumeric(numberTwo))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.sum(converter.convertToDouble(numberOne),converter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/substraction/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double substraction(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!converter.isNumeric(numberOne) || !converter.isNumeric(numberTwo))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.substraction(converter.convertToDouble(numberOne),converter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/multiplication/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double multiplication(@PathVariable(value = "numberOne") String numberOne,
                               @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!converter.isNumeric(numberOne) || !converter.isNumeric(numberTwo))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.multiplication(converter.convertToDouble(numberOne),converter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/division/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double division(@PathVariable(value = "numberOne") String numberOne,
                                 @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!converter.isNumeric(numberOne) || !converter.isNumeric(numberTwo))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.division(converter.convertToDouble(numberOne),converter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/mean/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double mean(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!converter.isNumeric(numberOne) || !converter.isNumeric(numberTwo))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.mean(converter.convertToDouble(numberOne),converter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/squareRoot/{number}", method = RequestMethod.GET)
    public Double squareRoot(@PathVariable(value = "number") String number) throws Exception {
        if(!converter.isNumeric(number))throw new UnsupportedMathOperationException("Please set a numeric value!");

        return math.squareRoot(converter.convertToDouble(number));
    }




}
