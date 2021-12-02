import React from "react";
import ReactECharts from "echarts-for-react";

const StockValueChart = ({ labels, data }) => {
  const options = {
    grid: { top: 8, right: 8, bottom: 24, left: 36 },
    xAxis: {
      type: "category",
      data: labels,
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: data,
        type: "line",
        smooth: true,
      },
    ],
    tooltip: {
      trigger: "axis",
    },
    color: "#ff8a50"
  };

  return <ReactECharts option={options} />;
};

export default StockValueChart;
