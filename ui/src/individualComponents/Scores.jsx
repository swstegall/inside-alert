import React from "react";
import { format } from "date-fns";
import { Typography } from "@mui/material";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import formatCurrency from "../utilities/formatCurrency";
import StockScoreChart from "./StockScoreChart";

const FormattedPortfolioScore = ({ score }) => {
  if (score >= 1.25) {
    return <div style={{ color: "red" }}>{score}</div>;
  } else {
    return <div>{score.toFixed(3)}</div>;
  }
};

const FormattedIndividualScore = ({ score }) => {
  if (score >= 1.5) {
    return <div style={{ color: "red" }}>{score}</div>;
  } else {
    return <div>{score.toFixed(3)}</div>;
  }
};

const Scores = ({ scores }) => {
  const portfolioAverageScore = 0;
  let counter = 0;
  let scoreCounter = null;
  const scoreElements = [];
  Object.keys(scores).forEach((key) => {
    scoreCounter += scores[key].average_score;
    ++counter;
    const reversedScores = [];
    for (let i = scores[key].values.length - 1; i >= 0; i--) {
      reversedScores.push(scores[key].values[i]);
    }
    scoreElements.push(
      <>
        <div className={"row pt-4"}>
          <div className={"col d-flex align-items-center"}>
            <Typography variant={"h4"}>{key}</Typography>
          </div>
        </div>
        <div className={"row pt-4"}>
          <div className={"col d-flex align-items-center"}>
            <Typography variant={"h5"}>
              Purchase Price: {formatCurrency(scores[key].purchase_price)}
            </Typography>
          </div>
        </div>
        <div className={"row pt-4"}>
          <div className={"col d-flex align-items-center"}>
            <Typography variant={"h5"}>
              Average Score:
              <FormattedIndividualScore score={scores[key].average_score} />
            </Typography>
          </div>
        </div>
        <div className={"row pt-4"}>
          <div className={"col"}>
            <StockScoreChart
              labels={scores[key].values.map((s) =>
                format(new Date(s.time), "Pp")
              )}
              data={scores[key].values.map((s) => Number(s.score.toFixed(3)))}
            />
          </div>
        </div>
        <div className={"row pt-4"}>
          <div className={"col"}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Time</TableCell>
                  <TableCell>Price</TableCell>
                  <TableCell>Score</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {reversedScores.map((s) => (
                  <TableRow>
                    <TableCell>{format(new Date(s.time), "Pp")}</TableCell>
                    <TableCell>{formatCurrency(s.current_price)}</TableCell>
                    <TableCell>
                      <FormattedIndividualScore score={s.score} />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </div>
      </>
    );
  });
  portfolioAverageScore = scoreCounter / counter;

  return (
    <>
      <div className={"row pt-4"}>
        <div className={"col d-flex align-items-center"}>
          <Typography variant={"h3"}>
            Portfolio Average Score:
            <FormattedPortfolioScore score={portfolioAverageScore} />
          </Typography>
        </div>
      </div>
      {scoreElements}
    </>
  );
};

export default Scores;
