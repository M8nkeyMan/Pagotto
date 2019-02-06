using System;
using System.Windows;
using System.Windows.Controls;

namespace FibonacciFinoA
{
    public partial class MainWindow : Window
    {
        private Fibonacci fib;
        private ListView listFib;

        public MainWindow()
        {
            fib = new Fibonacci();
            InitializeComponent();
            listFib = (ListView) lst_fib;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            fib.reset();
            String text = txt_maxN.Text;
            int maxN = Int32.Parse(text);

            while(fib.next < maxN)
            {
                fib.gen();
            }

            lst_fib.ItemsSource = fib.list;
        }
    }
}
