using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FibonacciFinoA
{
    class Fibonacci
    {
        public List <int> list { get; private set; }
        public int next { get; private set; }

        public Fibonacci()
        {
            reset();
        }

        public void gen()
        {
            list.Add(next);
            next = (list.Count == 1) ? 1 : list[list.Count - 1] + list[list.Count - 2];
        }

        public void reset()
        {
            list = new List<int>();
            next = 0;
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            foreach(int i in list)
            {
                sb.Append(i + "\n");
            }
            return sb.ToString();
        }
    }
}
