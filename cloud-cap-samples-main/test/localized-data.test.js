const cds = require('@sap/cds/lib')
const { GET, expect } = cds.test.run ('serve', __dirname+'/localized-data.cds', '--in-memory')
if (cds.User.default) cds.User.default = cds.User.Privileged // hard core monkey patch
else cds.User = cds.User.Privileged // hard core monkey patch for older cds releases

describe('Localized Data', () => {

  it('serves localized $metadata documents', async () => {
    const { data } = await GET`/browse/$metadata?sap-language=de`
    expect(data).to.contain('<Annotation Term="Common.Label" String="Währung"/>')
  })

  it('supports sap-language param', async () => {
    const { data } = await GET(`/browse/Books?$select=title,authorName` + '&sap-language=de')
    expect(data.value).to.containSubset([
      { title: 'Sturmhöhe', authorName: 'Emily Brontë' },
      { title: 'Jane Eyre', authorName: 'Charlotte Brontë' },
      { title: 'The Raven', authorName: 'Edgar Allen Poe' },
      { title: 'Eleonora', authorName: 'Edgar Allen Poe' },
      { title: 'Catweazle', authorName: 'Richard Carpenter' },
    ])
  })

  it('supports accept-language header', async () => {
    const { data } = await GET(`/browse/Books?$select=title,authorName`, {
      headers: { 'Accept-Language': 'de' },
    })
    expect(data.value).to.containSubset([
      { title: 'Sturmhöhe', authorName: 'Emily Brontë' },
      { title: 'Jane Eyre', authorName: 'Charlotte Brontë' },
      { title: 'The Raven', authorName: 'Edgar Allen Poe' },
      { title: 'Eleonora', authorName: 'Edgar Allen Poe' },
      { title: 'Catweazle', authorName: 'Richard Carpenter' },
    ])
  })

  it('supports queries with $expand', async () => {
    const { data } = await GET(`/browse/Books?&$select=title,authorName&$expand=currency`, {
      headers: { 'Accept-Language': 'de' },
    })
    expect(data.value).to.containSubset([
      { title: 'Sturmhöhe', authorName: 'Emily Brontë', currency: { name: 'Pfund' } },
      { title: 'Jane Eyre', authorName: 'Charlotte Brontë', currency: { name: 'Pfund' } },
      { title: 'The Raven', authorName: 'Edgar Allen Poe', currency: { name: 'US-Dollar' } },
      { title: 'Eleonora', authorName: 'Edgar Allen Poe', currency: { name: 'US-Dollar' } },
      { title: 'Catweazle', authorName: 'Richard Carpenter', currency: { name: 'Yen' } },
    ])
  })

  it('supports queries with nested $expand', async () => {
    const { data } = await GET(`/admin/Authors`, {
      params: {
        $filter: `startswith(name,'E')`,
        $expand: `books(
          $select=title;
          $expand=currency(
            $select=name,symbol
          )
        )`.replace(/\s/g, ''),
        $select: `name`,
      },
      headers: { 'Accept-Language': 'de' },
    })
    expect(data.value).to.containSubset([
      {
        name: 'Emily Brontë',
        books: [{ title: 'Sturmhöhe', currency: { name: 'Pfund', symbol: '£' } }],
      },
      {
        name: 'Edgar Allen Poe',
        books: [
          { title: 'The Raven', currency: { name: 'US-Dollar', symbol: '$' } },
          { title: 'Eleonora',  currency: { name: 'US-Dollar', symbol: '$' } },
        ],
      },
    ])
  })

  it('supports @cds.localized:false', async ()=>{
    const { data } = await GET(`/browse/BooksSans?&$select=title,localized_title&$expand=currency&$filter=locale eq 'de' or locale eq null`, {
      headers: { 'Accept-Language': 'de' },
    })
    expect(data.value).to.containSubset([
      { title: 'Wuthering Heights', localized_title: 'Sturmhöhe', currency: { name: 'British Pound' } },
      { title: 'Jane Eyre', currency: { name: 'British Pound' } },
      { title: 'The Raven', currency: { name: 'US Dollar' } },
      { title: 'Eleonora',  currency: { name: 'US Dollar' } },
      { title: 'Catweazle', currency: { name: 'Yen' } },
    ])
  })
})
