const axios = require('axios');

class Position {
  constructor() {}
  getData = async () => {
    let opts = {
      method: 'get',
      url: `http://localhost:4004/getPlotChart`,
      headers: {
        'Content-Type': 'application/json',
      },
    };
    const { data: positions } = await axios(opts);
    console.log(JSON.stringify(positions));

    const items = positions.map((item) => {
      return { x: item.positionX, y: item.positionY, v: item['cave']['vin'].name };
    });
    console.log(items);

    const data = {
      datasets: [
        {
          label: 'My Matrix',
          data: items,
          backgroundColor: [
            'rgba(255, 26, 104, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(0, 0, 0, 0.2)'
          ],
          borderColor: [
            'rgba(255, 26, 104, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(0, 0, 0, 1)'
          ],
          borderWidth: 1,
          width: ({ chart }) => (chart.chartArea || {}).width / 7 - 1,
          height: ({ chart }) => (chart.chartArea || {}).height / 7 - 1,
        },
      ],
    };
    return data;
  };

  getConfig = (data) => {
    return {
      type: 'matrix',
      data,
      options: {
        plugins: {
          legend: false,
          tooltip: {
            callbacks: {
              title() {
                return '';
              },
              label(context) {
                const v = context.dataset.data[context.dataIndex];
                return ['x: ' + v.x, 'y: ' + v.y, 'v: ' + v.v];
              },
            },
          },
        },
        scales: {
          x: {
            ticks: {
              stepSize: 1,
            },
            grid: {
              display: true,
            },
          },
          y: {
            offset: true,
            ticks: {
              stepSize: 1,
            },
            grid: {
              display: true,
            },
          },
        },
      },
    };
  };
}

export { Position }
