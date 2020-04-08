import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {

    /*class for interval:
     * start - start time
     * end - end time
     * person - person assigned for the interval
     * index - order in which the interval was input (needed while posting back the output)
     */
    static class Interval {
        int index;
        int start;
        int end;
        char person;

        Interval(int s, int e, int i) {
            start = s;
            end = e;
            person = 'k'; //dummy
            index = i;
        }

        // if isAvailable is true, the interval is not yet assigned
        public boolean isAvailable() {
            if (person == 'k')
                return true;
            return false;
        }

        public char getPerson() {

            return person;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String toString() {
            return this.getStart() + "@" + this.getEnd();
        }

    }


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // T is no. of tests
        int T = Integer.parseInt(bufferedReader.readLine().trim());
        int count = 0;
        for (int j = 0; j < T; j++) {
            count++;
            //  N is the no. of intervals
            int N = Integer.parseInt(bufferedReader.readLine().trim());

            // oPQ is a priority queue of min-heap to get intervals starting from minimum start time
            PriorityQueue<Interval> oPQ = new PriorityQueue<>(new Comparator<Interval>() {
                @Override
                public int compare(Interval interval, Interval t1) {
                    if (interval.start > t1.start) return 1;
                    else if (interval.start < t1.start) return -1;
                    else return 0;
                }
            });

            for (int i = 0; i < N; i++) {
                String[] s = bufferedReader.readLine().split(" ");
                Interval newitn = new Interval(Integer.parseInt(s[0]), Integer.parseInt(s[1]), i);
                oPQ.add(newitn);

            }
            // oFinal is a priority queue to get output in the order in which it was input (using index)
            PriorityQueue<Interval> oFinal = new PriorityQueue<>(new Comparator<Interval>() {
                @Override
                public int compare(Interval interval, Interval t1) {
                    if (interval.index > t1.index) return 1;
                    else if (interval.index < t1.index) return -1;
                    else return 0;
                }
            });

            //max heap for max end time after assigning task to C
            PriorityQueue<Interval> cHeap = new PriorityQueue<>(new Comparator<Interval>() {
                @Override
                public int compare(Interval interval, Interval t1) {
                    if (interval.end < t1.end) return 1;
                    else if (interval.end > t1.end) return -1;
                    else return 0;
                }
            });

            //max heap for max end time after assigning task to J
            PriorityQueue<Interval> jHeap = new PriorityQueue<>(new Comparator<Interval>() {
                @Override
                public int compare(Interval interval, Interval t1) {
                    if (interval.end < t1.end) return 1;
                    else if (interval.end > t1.end) return -1;
                    else return 0;
                }
            });

            Interval slotC = null;
            Interval slotJ = null;
            Interval temp = null;
            boolean notImpossible = true;
            //while unprocessed intervals exist
            while (!oPQ.isEmpty()) {
                //get interval with the minimum start time
                temp = oPQ.remove();

                //if C was not assigned any tasks, assign to C
                if (slotC == null) {
                    slotC = temp;
                    temp.person = 'C';
                    cHeap.add(temp);

                } else {
                    slotC = cHeap.peek();

                    //if C was assigned task, check if the last end time < current temp start time
                    if (temp.start >= slotC.end) {
                        slotC = temp;
                        temp.person = 'C';
                        cHeap.add(temp);
                    }
                }
                //if temp is not yet assigned, check for J
                if (temp.isAvailable()) {
                    if (slotJ == null) {
                        slotJ = temp;
                        temp.person = 'J';
                        jHeap.add(temp);
                    } else {
                        slotJ = jHeap.peek();

                        if (temp.start >= slotJ.end) {
                            slotJ = temp;
                            temp.person = 'J';
                            jHeap.add(temp);
                        }
                    }
                }
                //if temp is not yet assigned it means this is impossible
                if (temp.isAvailable()) {
                    System.out.print("Case #" + count + ": IMPOSSIBLE\n");
                    notImpossible = false;
                    break;
                }
                oFinal.add(temp);
            }
            if (notImpossible && N != 0) {
                System.out.print("Case #" + count + ": ");
                for (Interval itm : oFinal) {
                    System.out.print(itm.person);
                }
                if (j != T - 1) {
                    System.out.print("\n");
                }
            }
        }

        bufferedReader.close();
    }
}
