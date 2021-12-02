let formatter = new Intl.NumberFormat(undefined, {
  style: "currency",
  currency: "USD",
});

const formatCurrency = (value) => {
  return formatter.format(Number(value));
};

export default formatCurrency;
