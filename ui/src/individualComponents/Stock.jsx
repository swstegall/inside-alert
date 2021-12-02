import React from "react";
import { format } from "date-fns";
import Typography from "@mui/material/Typography";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import formatCurrency from "../utilities/formatCurrency";
import StockValueChart from "./StockValueChart";

const Stock = ({ title, values }) => {
  const target = values.find((v) => v.time === "current");
  const filteredValues = values.filter((v) => v.time !== "current");
  filteredValues.push(target);
  const reversedValues = [];
  for (let i = filteredValues.length - 1; i > 0; i--) {
    reversedValues.push(filteredValues[i]);
  }
  const chartLabels = filteredValues.map((f) =>
    f.time !== "current" ? format(new Date(f.time), "Pp") : "Current"
  );
  const chartData = filteredValues.map((f) => f.current_price);

  return (
    <>
      <div className={"row pt-4"}>
        <div className={"col d-flex align-items-center"}>
          <Typography variant={"h3"}>{title}</Typography>
        </div>
      </div>
      <div className={"row pt-4"}>
        <div className={"col"}>
          <StockValueChart labels={chartLabels} data={chartData} />
        </div>
      </div>
      <div className={"row pt-4"}>
        <div className={"col"}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Time</TableCell>
                <TableCell>Price</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {reversedValues.map((v) => (
                <TableRow>
                  <TableCell>
                    {v.time !== "current"
                      ? format(new Date(v.time), "Pp")
                      : "Current"}
                  </TableCell>
                  <TableCell>{formatCurrency(v.current_price)}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>
    </>
  );
};

export default Stock;
