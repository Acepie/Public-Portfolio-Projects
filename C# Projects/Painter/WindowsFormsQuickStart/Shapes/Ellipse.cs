using System;
using System.Drawing;

namespace WindowsFormsQuickStart
{
    public class Ellipse : Shapes
    {
        private Point start;

        private Point end;

        private Color color;

        public Ellipse(Color color, Point end, Point start)
        {
            this.start = start;
            this.end = end;
            this.color = color;
        }

        public void drawShape(Graphics g)
        {
            var pen = new Pen(color, 5.0f);

            var topCorner = new Point(Math.Min(start.X, end.X), Math.Min(start.Y, end.Y));

            var width = Math.Abs(start.X - end.X);
            var height = Math.Abs(start.Y - end.Y);
            g.DrawEllipse(pen, topCorner.X, topCorner.Y, width, height);
            pen.Dispose();
        }
    }
}