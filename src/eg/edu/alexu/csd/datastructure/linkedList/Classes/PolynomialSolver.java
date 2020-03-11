package eg.edu.alexu.csd.datastructure.linkedList.Classes;

import eg.edu.alexu.csd.datastructure.linkedList.Interfaces.IPolynomialSolver;

public class PolynomialSolver implements IPolynomialSolver {

    @SuppressWarnings("unchecked")
    private SLinkedList<Term>[] polynomials = new SLinkedList[4];

    private static class Term {
        private Integer coefficient, exponent;

        public Term(int coefficient, int exponent) {
            this.coefficient = coefficient;
            this.exponent = exponent;
        }

    }
    private int[][] toArray (SLinkedList<Term> polynomial) {
        int size = polynomial.size();
        int[][] polyArray = new int[2][size];
        Term tempTerm;
        polynomial.resetNext();
        for (int i = 0; i < size; i++) {
            tempTerm = polynomial.getNext();
            polyArray[0][i] = tempTerm.coefficient;
            polyArray[1][i] = tempTerm.exponent;
        }
        return polyArray;
    }

    public PolynomialSolver() {
        polynomials[0] = new SLinkedList<>(); //A
        polynomials[1] = new SLinkedList<>(); //B
        polynomials[2] = new SLinkedList<>(); //C
        polynomials[3] = new SLinkedList<>(); //R
    }

    private int getIndex(char c) {
        int i;
        switch (c) {
            case 'A':
            case 'a':
                i = 0;
                break;
            case 'B':
            case 'b':
                i = 1;
                break;
            case 'C':
            case 'c':
                i = 2;
                break;
            case 'R':
            case 'r':
                i = 3;
                break;
            default:
                throw new RuntimeException();
        }
        return i;
    }

    /*
    TODO
    needs to be changed to sort while taking input from user
    also need to handle case if user inputs two terms with same exponent while sorting
    will probably use DLinkedList to take input and sort then change to int[][]
     */
    private void sort(int[][] terms) { //bubble sort
        int n = terms[0].length;
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (terms[1][j] < terms[1][j + 1]) {
                    temp = terms[1][j];
                    terms[1][j] = terms[1][j + 1];
                    terms[1][j + 1] = temp;
                    temp = terms[0][j];
                    terms[0][j] = terms[0][j + 1];
                    terms[0][j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped)
                break;
        }
    }

    @Override
    public void setPolynomial(char poly, int[][] terms) {
        clearPolynomial(poly);
        sort(terms);
        for (int i = 0; i < terms[0].length; i++) {
            if (terms[0][i] != 0)
                polynomials[getIndex(poly)].add(new Term(terms[0][i]/*coefficient*/, terms[1][i]/*exponent*/));
        }
    }

    @Override
    public String print(char poly) {
        SLinkedList<Term> tempPoly = polynomials[getIndex(poly)];
        if (tempPoly.size() == 0) return null;
        Term tempTerm = tempPoly.get(0);
        Integer tempCo = tempTerm.coefficient; //because it is used A LOT
        String sExponent = tempTerm.exponent == 0? "" : (tempTerm.exponent == 1? "x" :
                ("x^(" + tempTerm.exponent + ")"));
        StringBuilder expression;
        if (tempCo > 0) { //+ve coefficient
            expression = new StringBuilder((tempCo == 1 ? "" : tempCo) + sExponent);
        } else if (tempCo < 0) { //-ve coefficient
            expression = new StringBuilder((tempCo == -1 ? " - " : tempCo) + sExponent);
        } else { //coefficient = zero
            expression = new StringBuilder();
        }

        for (int i = 1; i < tempPoly.size(); i++) {

            tempTerm = tempPoly.getNext();
            tempCo = tempTerm.coefficient;
            sExponent = tempTerm.exponent == 0? "" : (tempTerm.exponent == 1? "x" :
                    ("x^(" + tempTerm.exponent + ")"));

            if (tempCo > 0) { //+ve coefficient
                expression.append(" + ");
                expression.append(sExponent.equals("")? tempCo:(tempCo == 1 ? "" : tempCo)).append(sExponent);

            } else if (tempCo < 0) { //-ve coefficient
                expression.append(" - ");
                expression.append(sExponent.equals("")? -1*tempCo:(tempCo == -1 ? "" : -1 * tempCo)).append(sExponent);

            }
            //zero coefficient : continue;
        }

        return expression.toString();
    }

    @Override
    public void clearPolynomial(char poly) {
        polynomials[getIndex(poly)].clear();
    }

    @Override
    public float evaluatePolynomial(char poly, float value) {
        SLinkedList<Term> tempPoly = polynomials[getIndex(poly)];
        Term tempTerm;
        float result = 0;
        tempPoly.resetNext();
        while (tempPoly.hasNext()) {
            tempTerm = tempPoly.getNext();
            result += tempTerm.coefficient * Math.pow(value, tempTerm.exponent);
        }
        return result;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    /*
    TODO
    must check that both polynomials not empty in main
    will probably need to create another method for that purpose
     */
    public int[][] add(char poly1, char poly2) {
        SLinkedList<Term> x = polynomials[getIndex(poly1)];
        SLinkedList<Term> y = polynomials[getIndex(poly2)];
        SLinkedList<Term> res = polynomials[getIndex('R')];
        x.resetNext(); y.resetNext(); res.clear();
        Term tempx, tempy;
        do {
            if (x.next().exponent > y.next().exponent) {
                tempx = x.getNext();
                res.add(new Term(tempx.coefficient, tempx.exponent));
            } else if (x.next().exponent < y.next().exponent) {
                tempy = y.getNext();
                res.add(new Term(tempy.coefficient, tempy.exponent));
            } else {
                tempx = x.getNext(); tempy = y.getNext();
                if (tempx.coefficient + tempy.coefficient == 0) continue;
                res.add(new Term(tempx.coefficient + tempy.coefficient, tempx.exponent));
            }
        } while (x.hasNext() && y.hasNext());
        if (x.hasNext()) {
            tempx = x.getNext();
            res.add(new Term(tempx.coefficient, tempx.exponent));
        } else if (y.hasNext()) {
            tempy = y.getNext();
            res.add(new Term(tempy.coefficient, tempy.exponent));
        }
        return toArray(res);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public int[][] subtract(char poly1, char poly2) {
        SLinkedList<Term> x = polynomials[getIndex(poly1)];
        SLinkedList<Term> y = polynomials[getIndex(poly2)];
        SLinkedList<Term> res = polynomials[getIndex('R')];
        x.resetNext(); y.resetNext(); res.clear();
        Term tempx, tempy;
        do {
            if (x.next().exponent > y.next().exponent) {
                tempx = x.getNext();
                res.add(new Term(tempx.coefficient, tempx.exponent));
            } else if (x.next().exponent < y.next().exponent) {
                tempy = y.getNext();
                res.add(new Term(-1*tempy.coefficient, tempy.exponent));
            } else {
                tempx = x.getNext(); tempy = y.getNext();
                if (tempx.coefficient - tempy.coefficient == 0) continue;
                res.add(new Term(tempx.coefficient - tempy.coefficient, tempx.exponent));
            }
        } while (x.hasNext() && y.hasNext());
        if (x.hasNext()) {
            tempx = x.getNext();
            res.add(new Term(tempx.coefficient, tempx.exponent));
        } else if (y.hasNext()) {
            tempy = y.getNext();
            res.add(new Term(-1*tempy.coefficient, tempy.exponent));
        }
        return toArray(res);
    }

    @Override
    public int[][] multiply(char poly1, char poly2) {
        return new int[0][];
    }

    public boolean isEmpty(char poly) {
        return polynomials[getIndex(poly)].isEmpty();
    }
}
