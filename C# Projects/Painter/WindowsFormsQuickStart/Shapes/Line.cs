using System.Drawing;

namespace WindowsFormsQuickStart
{
    public class Line : Shapes
    {
        private readonly Color _color;

        private readonly Point _start;

        private readonly Point _end;

        public Line(Color color, Point start, Point end)
        {
            this._color = color;
            this._start = start;
            this._end = end;
        }

        public void drawShape(Graphics g)
        {
            var pen = new Pen(_color, 5.0f);
            g.DrawLine(pen, _start, _end);
            pen.Dispose();
        }
    }
}